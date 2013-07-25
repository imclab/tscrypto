#include "cryptoki.h"

#include <cf/SignInitMethod.hpp>
#include <cf/SignMethod.hpp>
#include <cf/Method.hpp>

#include "Session.h"
#include "Application.h"
#include "Token.h"

#include <string>
#include <memory>

using namespace tcbhsm;

namespace
{
std::unique_ptr<Application> app;

bool appIsInited(void)
{
  return static_cast<bool>(app);
}
}

CK_RV C_Initialize(CK_VOID_PTR pInitArgs)
{
  CK_C_INITIALIZE_ARGS_PTR args =
    reinterpret_cast<CK_C_INITIALIZE_ARGS_PTR>(pInitArgs);

  if(appIsInited())
    return CKR_CRYPTOKI_ALREADY_INITIALIZED;

  if(args != nullptr) {
    return CKR_ARGUMENTS_BAD;
  }
  app.reset(new Application());

  return CKR_OK;
}

CK_RV C_Finalize(CK_VOID_PTR pReserved)
{
  app.reset(nullptr);
  return CKR_OK;
}

CK_RV C_GetSlotList(CK_BBOOL tokenPresent, CK_SLOT_ID_PTR pSlotList, CK_ULONG_PTR pulCount)
{
  if (!appIsInited())
    return CKR_CRYPTOKI_NOT_INITIALIZED;

  auto const & slotList = app->getSlotList();

  unsigned long bufSize = 0;
  if (tokenPresent == CK_TRUE) {
    for (auto const& pSlot: slotList) {
      bufSize += (pSlot->tokenIsPresent()? 1 : 0);
    }
  } else {
    bufSize = slotList.size();
  }

  if (pSlotList == NULL_PTR) {
    *pulCount = bufSize;
    return CKR_OK;
  }

  if (*pulCount < bufSize) {
    *pulCount = bufSize;
    return CKR_BUFFER_TOO_SMALL;
  }

  int i = 0;
  for (auto const& pSlot: slotList) {
    if (tokenPresent == CK_FALSE || pSlot->tokenIsPresent())
      pSlotList[i++] = pSlot->getId();
  }

  *pulCount = bufSize;

  return CKR_OK;
}

CK_RV C_GetSlotInfo(CK_SLOT_ID slotId, CK_SLOT_INFO_PTR pInfo)
{
  if (!appIsInited())
    return CKR_CRYPTOKI_NOT_INITIALIZED;
  if (!pInfo) {
    return CKR_ARGUMENTS_BAD;
  }

  try {
    const Slot & slot = app->getSlot(slotId);
    slot.getInfo(pInfo);
    return CKR_OK;
  } catch (std::exception & e) {
    app->errorLog(e.what());
    return CKR_SLOT_ID_INVALID;
  }
}

CK_RV C_GetTokenInfo(CK_SLOT_ID slotID, CK_TOKEN_INFO_PTR pInfo)
{
  if (!appIsInited())
    return CKR_CRYPTOKI_NOT_INITIALIZED;
  if (pInfo == nullptr) {
    return CKR_ARGUMENTS_BAD;
  }

  // Horrible workaround :D
  const Slot * slot; // Borrowed pointer
  try {
    slot = &(app->getSlot(slotID));
  } catch (std::exception & e) {
    app->errorLog(e.what());
    return CKR_SLOT_ID_INVALID;
  }

  const Token * token; // Borrowed pointer
  try {
    token = &(slot->getToken());
  } catch (std::exception & e) {
    app->errorLog(e.what());
    return CKR_TOKEN_NOT_PRESENT;
  }

  token->getInfo(pInfo);

  return CKR_OK;
}

CK_RV C_OpenSession(CK_SLOT_ID slotID, CK_FLAGS flags,
                    CK_VOID_PTR pApplication, CK_NOTIFY Notify,
                    CK_SESSION_HANDLE_PTR phSession)
{
  // Primero la verificacion de todos los casos degenerados
  if (!appIsInited())
    return CKR_CRYPTOKI_NOT_INITIALIZED;

  const Slot * slot;
  try {
    slot = &(app->getSlot(slotID));
  } catch (std::exception & e) {
    app->errorLog(e.what());
    return CKR_SLOT_ID_INVALID;
  }

  try {
    if (!(slot->getToken().isInited()))
      return CKR_TOKEN_NOT_RECOGNIZED;
  } catch (std::exception & e) {
    app->errorLog(e.what());
    return CKR_SLOT_ID_INVALID;
  }

  // TODO: realizar todas las validaciones necesarias.
  try {
    app->openSession(slotID, flags, pApplication, Notify, phSession);
  } catch (std::exception & e) {
    app->errorLog(e.what());
    return CKR_GENERAL_ERROR;
  }
  return CKR_OK;
}

CK_RV C_CloseSession(CK_SESSION_HANDLE hSession)
{
  if (!appIsInited())
    return CKR_CRYPTOKI_NOT_INITIALIZED;

  try {
    app->closeSession(hSession);
  } catch(std::exception & e) {
    app->errorLog(e.what());
    return CKR_GENERAL_ERROR;
  }

  return CKR_OK;
}

CK_RV C_GetSessionInfo(CK_SESSION_HANDLE hSession, CK_SESSION_INFO_PTR pInfo) {
  if (!appIsInited())
    return CKR_CRYPTOKI_NOT_INITIALIZED;

  try {
    if (pInfo != nullptr)
      app->getSession(hSession).getSessionInfo(pInfo);
    else
      return CKR_ARGUMENTS_BAD;
  } catch(std::exception & e) {
    app->errorLog(e.what());
    return CKR_SESSION_HANDLE_INVALID;
  }

  // NUNCA debería llegar acá.
  return CKR_GENERAL_ERROR;
}

CK_RV C_Login(CK_SESSION_HANDLE hSession,
              CK_USER_TYPE      userType,
              CK_UTF8CHAR_PTR   pPin,
              CK_ULONG          ulPinLen)
{
  if (!appIsInited())
    return CKR_CRYPTOKI_NOT_INITIALIZED;

  app->getSession(hSession).login(userType, pPin, ulPinLen);
  return CKR_OK;
}

CK_RV C_Logout(CK_SESSION_HANDLE hSession) {
  if (!appIsInited())
    return CKR_CRYPTOKI_NOT_INITIALIZED;

  app->getSession(hSession).logout();
  return CKR_OK;
}

//CK_RV C_SignInit(CK_SESSION_HANDLE hSession,
//                 CK_MECHANISM_PTR  pMechanism,
//                 CK_OBJECT_HANDLE  hKey)
//{
//
//  switch(*pMechanism) {
//    case CKM_SHA1_RSA_PKCS: // Solo se maneja este tipo de firma.
//      Session const& session = app->getSession(hSession);
//      long long keyHandle = session.getKeyHandle(hKey);
//
//      std::string mechanism = "SHA1WithRSA";
//      cf::SignInitMethod method(mechanism, hKey);
//      try {
//        std::unique_ptr<Connection> connection(session.createConnection());
//
//        method.execute(*connection);
//        cf::ResponseMessagePtr rm(method.getResponse());
//      } catch(std::exception& e) {
//        app->errorLog(e.what());
//        return CKR_FUNCTION_FAILED;
//      }
//
//      return CKR_OK;
//
//    default:
//      return CKR_MECHANISM_INVALID;
//  }
//}
//
//CK_RV C_Sign(CK_SESSION_HANDLE hSession,
//             CK_BYTE_PTR       pData,
//             CK_ULONG          ulDataLen,
//             CK_BYTE_PTR       pSignature,
//             CK_ULONG_PTR      pulSignatureLen)
//{
//  Session const& session = app->getSession(hSession);
//  std::string data = tcb::functions::toBase64(pData, ulDataLen);
//
//  cf::SignMethod method(data);
//  cf::ResponseMessagePtr rm;
//  try {
//    method.execute(session.getConnection());
//    rm = method.getResponse().release();
//  } catch(std::exception& e) {
//    app->errorLog(e.what());
//    return CKR_FUNCTION_FAILED;
//  }
//
//  std::string signedData = rm.getValue<std::string>("signedData");
//  tcb::Base64Value b64(signedData);
//
//  if (*pulSignatureLen < b64.getLength())
//    return CKR_BUFFER_TOO_SMALL;
//  else {
//    *pulSignatureLen = b64.getLength();
//    b64.copyBytes(pSignature);
//    return CKR_OK;
//  }
//}
