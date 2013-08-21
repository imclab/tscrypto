#include "cryptoki.h"

#include <cf/SignInitMethod.hpp>
#include <cf/SignMethod.hpp>
#include <cf/Method.hpp>

#include "Session.h"
#include "Application.h"
#include "Token.h"

#include <functional>
#include <algorithm>
#include <string>
#include <memory>

#include "TcbError.h"

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
        if (pSlot->tokenIsPresent()) {
          bufSize += 1;
        }
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
      
      if (!(token.isInited()))
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
  
  CK_RV C_GetSessionInfo(CK_SESSION_HANDLE hSession, CK_SESSION_INFO_PTR pInfo) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      if (pInfo != nullptr)
        app->getSession(hSession).getSessionInfo(pInfo);
      else
        return CKR_ARGUMENTS_BAD;
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
    if (phObject == nullptr)
      return CKR_ARGUMENTS_BAD;
    
    try {
      *phObject = app->getSession(hSession).createObject(pTemplate, ulCount);
    } catch (TcbError& e) {
      return error(e);
    }
    
    return CKR_GENERAL_ERROR;
  }
  
  CK_RV C_DestroyObject (CK_SESSION_HANDLE hSession, CK_OBJECT_HANDLE hObject) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    
    try {
      app->getSession(hSession).destroyObject(hObject);
    } catch (TcbError & e) {
      return error(e);
    }
    
    return CKR_GENERAL_ERROR;
  }
  
  
  CK_RV C_FindObjectsInit(CK_SESSION_HANDLE hSession, CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) {
    if (!appIsInited())
      return CKR_CRYPTOKI_NOT_INITIALIZED;
    if (pTemplate == nullptr)
      return CKR_ARGUMENTS_BAD;
    
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
      std::vector<CK_OBJECT_HANDLE> handles (app->getSession(hSession).findObjects(ulMaxObjectCount));
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
    
    if (phPublicKey == nullptr || phPrivateKey == nullptr)
      return CKR_ARGUMENTS_BAD;
    
    try {
      KeyPair keysHandle = app->getSession(hSession).generateKeyPair(pMechanism, pPublicKeyTemplate, ulPublicKeyAttributeCount,
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
}