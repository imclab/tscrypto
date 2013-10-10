#include "config.h"

#include "cryptoki.h"

#include <cf/SignInitMethod.hpp>
#include <cf/SignMethod.hpp>
#include <cf/Method.hpp>

#include "Session.h"
#include "Application.h"
#include "Token.h"
#include "TcbError.h"

#include <functional>
#include <algorithm>
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
  
  CK_RV error(TcbError &e) {
    app->errorLog(e.what());
    return e.getErrorCode();
  }
}

/***
 * - ¿Cómo se estructuran las funciones de este archivo?
 *  - Primero se verifica si la aplicacion está iniciada
 *  - Luego se verifican todos los argumentos que sean sólo utilizados por la función.
 *    Si el argumento es pasado a otra función es esa la encargada de verificar cada error.
 *  - Por esto, toda llamada a una funcion debe estar envuelta en un try-catch de TcbError's
 *  - Al final de cada funcion debe estar el "return CKR_OK;"
 *  - Mantener la lógica de estas funciones lo más sencilla posible.
 ***/

extern "C" {
  
  CK_FUNCTION_LIST functionList = {
    { 2, 20 },
    C_Initialize,
    C_Finalize,
    C_GetInfo,
    C_GetFunctionList,
    C_GetSlotList,
    C_GetSlotInfo,
    C_GetTokenInfo,
    C_GetMechanismList,
    C_GetMechanismInfo,
    C_InitToken,
    C_InitPIN,
    C_SetPIN,
    C_OpenSession,
    C_CloseSession,
    C_CloseAllSessions,
    C_GetSessionInfo,
    C_GetOperationState,
    C_SetOperationState,
    C_Login,
    C_Logout,
    C_CreateObject,
    C_CopyObject,
    C_DestroyObject,
    C_GetObjectSize,
    C_GetAttributeValue,
    C_SetAttributeValue,
    C_FindObjectsInit,
    C_FindObjects,
    C_FindObjectsFinal,
    C_EncryptInit,
    C_Encrypt,
    C_EncryptUpdate,
    C_EncryptFinal,
    C_DecryptInit,
    C_Decrypt,
    C_DecryptUpdate,
    C_DecryptFinal,
    C_DigestInit,
    C_Digest,
    C_DigestUpdate,
    C_DigestKey,
    C_DigestFinal,
    C_SignInit,
    C_Sign,
    C_SignUpdate,
    C_SignFinal,
    C_SignRecoverInit,
    C_SignRecover,
    C_VerifyInit,
    C_Verify,
    C_VerifyUpdate,
    C_VerifyFinal,
    C_VerifyRecoverInit,
    C_VerifyRecover,
    C_DigestEncryptUpdate,
    C_DecryptDigestUpdate,
    C_SignEncryptUpdate,
    C_DecryptVerifyUpdate,
    C_GenerateKey,
    C_GenerateKeyPair,
    C_WrapKey,
    C_UnwrapKey,
    C_DeriveKey,
    C_SeedRandom,
    C_GenerateRandom,
    C_GetFunctionStatus,
    C_CancelFunction,
    C_WaitForSlotEvent
  };
  
  extern CK_FUNCTION_LIST functionList;
  
  CK_RV C_Initialize(CK_VOID_PTR pInitArgs)
  {
    CK_C_INITIALIZE_ARGS_PTR args =
    reinterpret_cast<CK_C_INITIALIZE_ARGS_PTR>(pInitArgs);
    
    if(appIsInited())
      return CKR_CRYPTOKI_ALREADY_INITIALIZED;
    
    if (args != nullptr) {
      if (args->pReserved != nullptr)
        return CKR_ARGUMENTS_BAD;
      
      bool someButNotAll = 
      !(args->CreateMutex && 
      args->DestroyMutex && 
      args->LockMutex &&
      args->UnlockMutex)
      
      &&
      
      (args->CreateMutex ||
      args->DestroyMutex ||
      args->LockMutex ||
      args->UnlockMutex);
      
      if (someButNotAll)
        return CKR_ARGUMENTS_BAD;
    }
    try {
      app.reset(new Application());
    } catch (TcbError & e) {
      std::cerr << e.what() << std::endl;
      return e.getErrorCode();
    }
    
    return CKR_OK;
  }
  
  CK_RV C_Finalize(CK_VOID_PTR pReserved)
  {
    if (pReserved != nullptr)
      return CKR_ARGUMENTS_BAD;
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    app.reset(nullptr);
    return CKR_OK;
  }
  
  CK_RV C_InitToken(CK_SLOT_ID slotID, CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen, CK_UTF8CHAR_PTR pLabel) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    if (!pPin) {
      return CKR_ARGUMENTS_BAD;
    }
    
    if (!pLabel) {
      return CKR_ARGUMENTS_BAD;
    }
    
    try {
      std::string label ( reinterpret_cast<char*>(pLabel), 32 );
      std::string pin ( reinterpret_cast<char*>(pPin), ulPinLen );
      app->getSlot(slotID).initToken(label, pin);
    } catch (TcbError & e) {
      return error(e);
    }
    
    return CKR_OK;
  }
  
  CK_RV C_InitPIN(CK_SESSION_HANDLE hSession,CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen) {
   if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    if (!pPin) {
      return CKR_ARGUMENTS_BAD;
    }
    
    try {
      std::string pin ( reinterpret_cast<char*>(pPin), ulPinLen );
      app->getSession(hSession).getCurrentSlot().getToken().setUserPin(pin);
    } catch (TcbError & e) {
      return error(e);
    }
    
    return CKR_OK;
    
  }
  
  CK_RV C_SetPIN(CK_SESSION_HANDLE hSession, CK_UTF8CHAR_PTR pOldPin, CK_ULONG ulOldLen, CK_UTF8CHAR_PTR pNewPin, CK_ULONG ulNewLen) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_GetInfo(CK_INFO_PTR pInfo) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    if (pInfo == nullptr)
      return CKR_ARGUMENTS_BAD;
    
    std::string manufacturer { "NicLabs" };
    std::string description { "Implementacion de PKCS11" };
    
    pInfo->cryptokiVersion.major = 2;
    pInfo->cryptokiVersion.minor = 20;
    
    std::fill(pInfo->manufacturerID,
              pInfo->manufacturerID + 32, 
              ' ');
    std::copy(manufacturer.cbegin(),
              manufacturer.cend(),
              pInfo->manufacturerID);
    
    std::fill(pInfo->libraryDescription,
              pInfo->libraryDescription + 32, 
              ' ');
    std::copy(description.cbegin(),
              description.cend(),
              pInfo->libraryDescription);
    
    pInfo->flags = 0;
    pInfo->libraryVersion.major = VERSION_MAJOR;
    pInfo->libraryVersion.minor = VERSION_MINOR;
    

    return CKR_OK;
  }
  
  CK_RV C_GetFunctionList(CK_FUNCTION_LIST_PTR_PTR ppFunctionList) {   
    if (ppFunctionList == nullptr)
      return CKR_ARGUMENTS_BAD;
    
    *ppFunctionList = &functionList;
    
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
        if (pSlot->tokenIsPresent()) {
          bufSize += 1;
        }
      }
    } else {
      bufSize = slotList.size();
    }
    
    if (pulCount == nullptr) {
      return CKR_ARGUMENTS_BAD;
    }
      
    if (pSlotList == nullptr) {
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
      app->getSlot(slotId).getInfo(pInfo);
    } catch (TcbError & e) {
      return error(e);
    }
    
    return CKR_OK;
  }
  
  CK_RV C_GetTokenInfo(CK_SLOT_ID slotID, CK_TOKEN_INFO_PTR pInfo)
  {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    if (pInfo == nullptr)
      return CKR_ARGUMENTS_BAD;
    
    try {
      app->getSlot(slotID).getToken().getInfo(pInfo);
    } catch (TcbError & e) {
      return error(e);
    }
    
    return CKR_OK;
    
    
  }
  
  CK_RV C_OpenSession(CK_SLOT_ID slotID, CK_FLAGS flags,
                      CK_VOID_PTR pApplication, CK_NOTIFY Notify,
                      CK_SESSION_HANDLE_PTR phSession)
  {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      Token & token = app->getSlot(slotID).getToken();
      
      if (!token.isInited())
        return CKR_TOKEN_NOT_RECOGNIZED;
      
      app->openSession(slotID, flags, pApplication, Notify, phSession);
    } catch (TcbError & e) {
      return error(e);
    }
    
    return CKR_OK;
  }
  
  CK_RV C_CloseSession(CK_SESSION_HANDLE hSession)
  {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      app->closeSession(hSession);
    } catch(TcbError & e) {
      return error(e);
    }
    
    return CKR_OK;
  }
  
  CK_RV C_CloseAllSessions(CK_SLOT_ID slotID) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      app->closeAllSessions(slotID);
    } catch(TcbError & e) {
      return error(e);
    }
    
    return CKR_OK;
  }
  
  CK_RV C_GetSessionInfo(CK_SESSION_HANDLE hSession, CK_SESSION_INFO_PTR pInfo) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      app->getSession(hSession).getSessionInfo(pInfo);
    } catch(TcbError & e) {
      return error(e);
    }
    
    return CKR_OK;
  }
  
  CK_RV C_Login(CK_SESSION_HANDLE hSession,
                CK_USER_TYPE      userType,
                CK_UTF8CHAR_PTR   pPin,
                CK_ULONG          ulPinLen)
  {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      app->getSession(hSession).login(userType, pPin, ulPinLen);
    } catch (TcbError & e) {
      return error(e);
    }
    return CKR_OK;
  }
  
  CK_RV C_Logout(CK_SESSION_HANDLE hSession) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      app->getSession(hSession).logout();
    } catch (TcbError &e) {
      return error(e);
    }
    return CKR_OK;
  }
  
  CK_RV C_CreateObject (CK_SESSION_HANDLE hSession, CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount, CK_OBJECT_HANDLE_PTR phObject) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      Session &session = app->getSession(hSession);
      
      if (phObject == nullptr)
        return CKR_ARGUMENTS_BAD;
      
      *phObject = session.createObject(pTemplate, ulCount);
    } catch (TcbError& e) {
      return error(e);
    }
    
    return CKR_OK;
  }
  
  CK_RV C_DestroyObject (CK_SESSION_HANDLE hSession, CK_OBJECT_HANDLE hObject) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      app->getSession(hSession).destroyObject(hObject);
    } catch (TcbError & e) {
      return error(e);
    }
    
    return CKR_OK;
  }
  
  
  CK_RV C_FindObjectsInit(CK_SESSION_HANDLE hSession, CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      app->getSession(hSession).findObjectsInit(pTemplate, ulCount);
    } catch (TcbError & e) {
      return error(e);
    }
    
    return CKR_OK;
  }
  
  CK_RV C_FindObjects(CK_SESSION_HANDLE hSession, CK_OBJECT_HANDLE_PTR phObject, CK_ULONG ulMaxObjectCount, CK_ULONG_PTR pulObjectCount) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      Session & session = app->getSession(hSession);
      
      if (phObject == nullptr || pulObjectCount == nullptr)
        return CKR_ARGUMENTS_BAD;
      
      std::vector<CK_OBJECT_HANDLE> handles (session.findObjects(ulMaxObjectCount));
      // handles tiene a lo mas ulMaxObjectCount elementos, por lo que no hay que verificar.
      int i = 0;
      for (auto& handle: handles) {
        phObject[i] = handle;
        ++i;
      }
      *pulObjectCount = i;
      
    } catch (TcbError & e) {
      return error(e);
    } 
    
    return CKR_OK;
  }
  
  CK_RV C_FindObjectsFinal(CK_SESSION_HANDLE hSession) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      app->getSession(hSession).findObjectsFinal();
    } catch (TcbError & e) {
      return error(e);
    } 
    
    return CKR_OK;
  }
  
  CK_RV C_GetAttributeValue(CK_SESSION_HANDLE sessionHandle, CK_OBJECT_HANDLE objectHandle, CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      app->getSession(sessionHandle).getObject(objectHandle).copyAttributes(pTemplate, ulCount);
    } catch (TcbError & e) {
      return error(e);
    }
    
    return CKR_OK;
  }
  
  CK_RV C_GenerateKeyPair(CK_SESSION_HANDLE hSession, CK_MECHANISM_PTR pMechanism, CK_ATTRIBUTE_PTR pPublicKeyTemplate, 
                          CK_ULONG ulPublicKeyAttributeCount, CK_ATTRIBUTE_PTR pPrivateKeyTemplate, CK_ULONG ulPrivateKeyAttributeCount,
                          CK_OBJECT_HANDLE_PTR phPublicKey, CK_OBJECT_HANDLE_PTR phPrivateKey) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      Session& session = app->getSession(hSession);
      
      if (phPublicKey == nullptr || phPrivateKey == nullptr) // Si son nulos, no calculo la llave...
        return CKR_ARGUMENTS_BAD;
      
      KeyPair keysHandle = session.generateKeyPair(pMechanism, 
                                                                     pPublicKeyTemplate, ulPublicKeyAttributeCount,
                                                                     pPrivateKeyTemplate, ulPrivateKeyAttributeCount);
      
      *phPrivateKey = keysHandle.first;
      *phPublicKey = keysHandle.second;
      
    } catch (TcbError &e) {
      return error(e);
    }
    
    return CKR_OK;
  }
                          
  CK_RV C_SignInit(CK_SESSION_HANDLE hSession, CK_MECHANISM_PTR pMechanism, CK_OBJECT_HANDLE hKey) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
      
    try {
      app->getSession(hSession).signInit(pMechanism, hKey);
    } catch (TcbError &e) {
      return error(e);
    }
    
    return CKR_OK;
  }
  
  CK_RV C_Sign(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pData, CK_ULONG ulDataLen, CK_BYTE_PTR pSignature,
               CK_ULONG_PTR pulSignatureLen) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      app->getSession(hSession).sign(pData, ulDataLen, pSignature, pulSignatureLen);
    } catch (TcbError &e) {
      return error(e);
    }
    
    return CKR_OK;
  }
  
  CK_RV C_DigestInit(CK_SESSION_HANDLE hSession, CK_MECHANISM_PTR pMechanism) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      app->getSession(hSession).digestInit(pMechanism);
    } catch (TcbError &e) {
      return error(e);
    }
    
    return CKR_OK;
  }
  
  CK_RV C_Digest(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pData, CK_ULONG ulDataLen,
                 CK_BYTE_PTR pDigest, CK_ULONG_PTR pulDigestLen) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      app->getSession(hSession).digest(pData, ulDataLen, pDigest, pulDigestLen);
    } catch (TcbError &e) {
      return error(e);
    }
    
    return CKR_OK;
  }
  
  CK_RV C_SeedRandom(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pSeed, CK_ULONG ulSeedLen) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      app->getSession(hSession).seedRandom(pSeed, ulSeedLen);
    } catch (TcbError &e) {
      return error(e);
    }
    
    return CKR_OK;
  }
  
  CK_RV C_GenerateRandom(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pRandomData, CK_ULONG ulRandomLen) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      app->getSession(hSession).generateRandom(pRandomData, ulRandomLen);
    } catch (TcbError &e) {
      return error(e);
    }
    
    return CKR_OK;
  }
  
  // NOTE: FUNCIONES NO IMPLEMENTADAS
  CK_RV C_GetMechanismList(CK_SLOT_ID slotID, CK_MECHANISM_TYPE_PTR pMechanismList, CK_ULONG_PTR pulCount) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_GetMechanismInfo(CK_SLOT_ID slotID, CK_MECHANISM_TYPE type, CK_MECHANISM_INFO_PTR pInfo) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_GetOperationState(CK_SESSION_HANDLE, CK_BYTE_PTR, CK_ULONG_PTR) {  
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_SetOperationState(CK_SESSION_HANDLE, CK_BYTE_PTR, CK_ULONG, CK_OBJECT_HANDLE, CK_OBJECT_HANDLE) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_CopyObject(CK_SESSION_HANDLE, CK_OBJECT_HANDLE, CK_ATTRIBUTE_PTR, CK_ULONG, CK_OBJECT_HANDLE_PTR) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_GetObjectSize(CK_SESSION_HANDLE, CK_OBJECT_HANDLE, CK_ULONG_PTR) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_SetAttributeValue(CK_SESSION_HANDLE hSession, CK_OBJECT_HANDLE hObject, CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_EncryptInit(CK_SESSION_HANDLE hSession, CK_MECHANISM_PTR pMechanism, CK_OBJECT_HANDLE hKey) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_Encrypt(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pData, CK_ULONG ulDataLen, 
                  CK_BYTE_PTR pEncryptedData, CK_ULONG_PTR pulEncryptedDataLen) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_EncryptUpdate(CK_SESSION_HANDLE, CK_BYTE_PTR, CK_ULONG, CK_BYTE_PTR, CK_ULONG_PTR) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_EncryptFinal(CK_SESSION_HANDLE, CK_BYTE_PTR, CK_ULONG_PTR) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_DecryptInit(CK_SESSION_HANDLE hSession, CK_MECHANISM_PTR pMechanism, CK_OBJECT_HANDLE hKey) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_Decrypt(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pEncryptedData, CK_ULONG ulEncryptedDataLen, 
                  CK_BYTE_PTR pData, CK_ULONG_PTR pulDataLen) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_DecryptUpdate(CK_SESSION_HANDLE, CK_BYTE_PTR, CK_ULONG, CK_BYTE_PTR, CK_ULONG_PTR) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_DecryptFinal(CK_SESSION_HANDLE, CK_BYTE_PTR, CK_ULONG_PTR) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  
  CK_RV C_DigestUpdate(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pPart, CK_ULONG ulPartLen) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_DigestKey(CK_SESSION_HANDLE, CK_OBJECT_HANDLE) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_DigestFinal(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pDigest, CK_ULONG_PTR pulDigestLen) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_SignRecoverInit(CK_SESSION_HANDLE, CK_MECHANISM_PTR, CK_OBJECT_HANDLE) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }

  CK_RV C_SignRecover(CK_SESSION_HANDLE, CK_BYTE_PTR, CK_ULONG, CK_BYTE_PTR, CK_ULONG_PTR) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_VerifyInit(CK_SESSION_HANDLE hSession, CK_MECHANISM_PTR pMechanism, CK_OBJECT_HANDLE hKey) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_Verify(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pData, CK_ULONG ulDataLen, CK_BYTE_PTR pSignature,
                 CK_ULONG ulSignatureLen) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_VerifyUpdate(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pPart, CK_ULONG ulPartLen) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_VerifyFinal(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pSignature, CK_ULONG ulSignatureLen) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_VerifyRecoverInit(CK_SESSION_HANDLE, CK_MECHANISM_PTR, CK_OBJECT_HANDLE) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_VerifyRecover(CK_SESSION_HANDLE, CK_BYTE_PTR, CK_ULONG, CK_BYTE_PTR, CK_ULONG_PTR) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_DigestEncryptUpdate(CK_SESSION_HANDLE, CK_BYTE_PTR, CK_ULONG, CK_BYTE_PTR, CK_ULONG_PTR) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_DecryptDigestUpdate(CK_SESSION_HANDLE, CK_BYTE_PTR, CK_ULONG, CK_BYTE_PTR, CK_ULONG_PTR) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_SignEncryptUpdate(CK_SESSION_HANDLE, CK_BYTE_PTR, CK_ULONG, CK_BYTE_PTR, CK_ULONG_PTR) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_SignUpdate(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pPart, CK_ULONG ulPartLen) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_SignFinal(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pSignature, CK_ULONG_PTR pulSignatureLen) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_DecryptVerifyUpdate(CK_SESSION_HANDLE, CK_BYTE_PTR, CK_ULONG, CK_BYTE_PTR, CK_ULONG_PTR) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_GenerateKey(CK_SESSION_HANDLE, CK_MECHANISM_PTR, CK_ATTRIBUTE_PTR, CK_ULONG, CK_OBJECT_HANDLE_PTR) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_WrapKey(CK_SESSION_HANDLE, CK_MECHANISM_PTR, CK_OBJECT_HANDLE, CK_OBJECT_HANDLE, 
                  CK_BYTE_PTR, CK_ULONG_PTR) 
  {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
  
  CK_RV C_UnwrapKey(CK_SESSION_HANDLE, CK_MECHANISM_PTR, CK_OBJECT_HANDLE, CK_BYTE_PTR, CK_ULONG,
                    CK_ATTRIBUTE_PTR, CK_ULONG, CK_OBJECT_HANDLE_PTR) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
                    
  CK_RV C_DeriveKey(CK_SESSION_HANDLE, CK_MECHANISM_PTR, CK_OBJECT_HANDLE, CK_ATTRIBUTE_PTR, 
                    CK_ULONG, CK_OBJECT_HANDLE_PTR) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }  
  
  CK_RV C_GetFunctionStatus(CK_SESSION_HANDLE) {
    return CKR_FUNCTION_NOT_PARALLEL;
  }
  
  CK_RV C_CancelFunction(CK_SESSION_HANDLE) {
    return CKR_FUNCTION_NOT_PARALLEL;
  }
  
  CK_RV C_WaitForSlotEvent(CK_FLAGS, CK_SLOT_ID_PTR, CK_VOID_PTR) {
    return CKR_FUNCTION_NOT_SUPPORTED;
  }
}