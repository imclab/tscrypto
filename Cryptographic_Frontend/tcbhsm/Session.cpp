/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */


#include <cstdlib> // getenv
#include "tcbhsm/Session.h"
#include "cf/Method.hpp"
#include "cf/GenerateKeyPairMethod.hpp"
#include "cf/RabbitConnection.hpp"
#include "cf/ResponseMessage.hpp"
#include "TcbError.h"

using namespace tcbhsm;

namespace { // Funcion auxiliar

  CK_BBOOL userAuthorization(CK_STATE sessionState, CK_BBOOL isTokenObject, CK_BBOOL isPrivateObject, int userAction) {
    switch(sessionState) {
      case CKS_RW_SO_FUNCTIONS:
        if(isPrivateObject == CK_FALSE) {
          return CK_TRUE;
        } else {
          return CK_FALSE;
        }
        break;
      case CKS_RW_USER_FUNCTIONS:
        return CK_TRUE;
        break;
      case CKS_RO_USER_FUNCTIONS:
        if(isTokenObject == CK_TRUE) {
          if(userAction == 1) {
            return CK_FALSE;
          } else {
            return CK_TRUE;
          }
        } else {
          return true;
        }
        break;
      case CKS_RW_PUBLIC_SESSION:
        if(isPrivateObject == CK_FALSE) {
          return CK_TRUE;
        } else {
          return CK_FALSE;
        }
        break;
      case CKS_RO_PUBLIC_SESSION:
        if(isPrivateObject == CK_FALSE) {
          if(isTokenObject == CK_TRUE && userAction == 1) {
            return CK_FALSE;
          } else {
            return CK_TRUE;
          }
        } else {
          return CK_FALSE;
        }

        break;
      default:
        break;
    }

    return CK_FALSE;
  }

}

Session::Session(CK_FLAGS flags, CK_VOID_PTR pApplication, CK_NOTIFY notify, Slot & currentSlot)
  : actualObjectHandle_(0), refCount_(1), flags_(flags), application_(pApplication)
  , notify_(notify), currentSlot_(currentSlot)
{

}

auto Session::createConnection() -> ConnectionPtr &&
{
  // Ojo! La configuracion de la conexión esta hecha con variables de entorno...
  const char* hostname = std::getenv("TCB_HOSTNAME");
  const char* port = std::getenv("TCB_PORT");

  if (hostname == nullptr) {
    hostname = "localhost";
  }
  if (port == nullptr) {
    port = "5672";
  }

  int portNumber = std::stoi(port);
  
  return std::move(ConnectionPtr(new cf::RabbitConnection(hostname, portNumber, "", "rpc_queue", 1)));
}

void Session::retain()
{
    ++refCount_;
}

void Session::release()
{
    if (refCount_ > 0)
        --refCount_;
}

unsigned int Session::count()
{
    return refCount_;
}

void Session::getSessionInfo(CK_SESSION_INFO_PTR pInfo) const {
    if (pInfo != nullptr) {
        pInfo->slotID = currentSlot_.getId();
        pInfo->state = getState();
        pInfo->flags = getFlags();
        pInfo->ulDeviceError = 0;
    }
}

CK_OBJECT_HANDLE Session::createObject(CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) {
  if (pTemplate == nullptr)
    throw TcbError("Session::createObject", "pTemplate es nullptr.", CKR_ARGUMENTS_BAD);

  // Copiado de SoftHSM, valores por defecto:
  CK_BBOOL isToken = CK_FALSE;
  CK_BBOOL isPrivate = CK_TRUE;
  CK_OBJECT_CLASS oClass = CKO_VENDOR_DEFINED;
  CK_KEY_TYPE keyType = CKK_VENDOR_DEFINED;

  // Extract object information
  for(CK_ULONG i = 0; i < ulCount; i++) {
    switch(pTemplate[i].type) {
      case CKA_TOKEN:
        if(pTemplate[i].ulValueLen == sizeof(CK_BBOOL)) {
          isToken = *(CK_BBOOL*)pTemplate[i].pValue;
        }
        break;
      case CKA_PRIVATE:
        if(pTemplate[i].ulValueLen == sizeof(CK_BBOOL)) {
          isPrivate = *(CK_BBOOL*)pTemplate[i].pValue;
        }
        break;
      case CKA_CLASS:
        if(pTemplate[i].ulValueLen == sizeof(CK_OBJECT_CLASS)) {
          oClass = *(CK_OBJECT_CLASS*)pTemplate[i].pValue;
        }
        break;
      case CKA_KEY_TYPE:
        if(pTemplate[i].ulValueLen == sizeof(CK_KEY_TYPE)) {
          keyType = *(CK_KEY_TYPE*)pTemplate[i].pValue;
        }
        break;
      default:
        break;
    }
  }

  if (isToken == CK_TRUE && this->isReadOnly())
    throw TcbError("Session::createObject", "Only session objects can be created during a read-only session.", CKR_SESSION_READ_ONLY);

  CK_BBOOL userAuth = userAuthorization(this->getState(), isToken, isPrivate, 1);
  if(userAuth == CK_FALSE)
    throw TcbError("Session::createObject", "User is not authorized", CKR_USER_NOT_LOGGED_IN);

  CK_OBJECT_HANDLE oHandle = 0; // lol, por mientras.

  return oHandle;
}

void Session::destroyObject(CK_OBJECT_HANDLE hObject) {
  auto it = objects_.find(hObject);
  if (it != objects_.end()) {
    objects_.erase(it);
  } else {
    throw TcbError("Session::destroyObject", "Objeto no encontrado.", CKR_OBJECT_HANDLE_INVALID);
  }
}

void Session::findObjectsInit(CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) {
  // TODO: Verificar correctitud
  if (ulCount == 0) {
    // Busco todos los objetos...
    for (auto& handleObjectPair: objects_) {
      foundObjects.push_back(handleObjectPair.first);
    }
  } else {
    for (auto& handleObjectPair: objects_) {
      if (handleObjectPair.second->match(pTemplate, ulCount)) {
        foundObjects.push_back(handleObjectPair.first);
      }
    }
  }
  //TODO: verificar permisos de acceso.
  foundObjectsIterator = foundObjects.begin();
  foundObjectsEnd = foundObjects.end();

}

auto Session::findObjects(CK_ULONG maxObjectCount) -> std::vector<CK_OBJECT_HANDLE> {
  if (!findInitialized)
    throw TcbError("Session::findObjects", "No se inicio la busqueda.", CKR_OPERATION_NOT_INITIALIZED);

  auto end = foundObjectsIterator + maxObjectCount;
  if (foundObjectsEnd < end)
    end = foundObjectsEnd;

  std::vector<CK_OBJECT_HANDLE> response(foundObjectsIterator, end);
  foundObjectsIterator = end;
  return response;
}


SessionObject & Session::getObject(CK_OBJECT_HANDLE objectHandle) {
  try {
    SessionObject & object = *(objects_.at(objectHandle));
    return object;
  } catch (std::out_of_range &e) {
    throw TcbError("Session::getObject", "Objeto no existe en la sesion.", CKR_ARGUMENTS_BAD);
  }
}


CK_STATE Session::getState() const {
  // TODO: Completar la semántica de lecto-escritura.
  switch (currentSlot_.getToken().getSecurityLevel()) {
  case Token::SecurityLevel::SECURITY_OFFICER:
    return CKS_RW_SO_FUNCTIONS;
  case Token::SecurityLevel::USER:
    return CKS_RW_USER_FUNCTIONS;

  default:
  case Token::SecurityLevel::PUBLIC:
    return CKS_RW_PUBLIC_SESSION;
  }
}

bool Session::isReadOnly() const {
  return false;
}

CK_FLAGS Session::getFlags() const {
    return flags_;
}

void Session::login(CK_USER_TYPE userType, CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen) {
  Token & token = currentSlot_.getToken();
  token.login(userType, pPin, ulPinLen);
}

void Session::logout() {
  currentSlot_.getToken().logout();
}


/***
 * FUNCIONES CRIPTOGRAFICAS
 ***/

CK_OBJECT_HANDLE Session::generateKeyPair(CK_MECHANISM_PTR pMechanism, 
                                          CK_ATTRIBUTE_PTR pPublicKeyTemplate, CK_ULONG ulPublicKeyAttributeCount, 
                                          CK_ATTRIBUTE_PTR pPrivateKeyTemplate, CK_ULONG ulPrivateKeyAttributeCount) {
  // TODO: verificar permisos de acceso.
  if (pMechanism == nullptr || pPublicKeyTemplate == nullptr || pPrivateKeyTemplate == nullptr) {
    throw TcbError("Session::generateKeyPair", "Argumentos nulos", CKR_ARGUMENTS_BAD);
  }

  switch (pMechanism->mechanism) {
    case CKM_RSA_PKCS_KEY_PAIR_GEN:
      try {
        long long handler;
        
        cf::RabbitConnection connection("localhost", 5672, "", "rpc_queue", 1);
        cf::Method * method = new cf::GenerateKeyPairMethod("RSA", 4096, "65537");
        method->execute(connection);
        cf::ResponseMessagePtr response(method->getResponse());
        handler = response->getValue<long long>("handler");
        delete method;
        
        // NOTE: Esto está mas o menos copiado de SoftHSM...
        CK_OBJECT_CLASS oClass = CKO_PUBLIC_KEY;
        CK_KEY_TYPE keyType = CKK_RSA;
        CK_MECHANISM_TYPE mechType = CKM_RSA_PKCS_KEY_PAIR_GEN;
        CK_BBOOL ckTrue = CK_TRUE, ckFalse = CK_FALSE;
        CK_DATE emptyDate;
                
        // Atributos genéricos...
        // TODO: verificar que se copien los atributos y no solo su puntero...
        CK_ATTRIBUTE aClass = { CKA_CLASS, &oClass, sizeof(oClass) };
        CK_ATTRIBUTE aKeyType = { CKA_KEY_TYPE, &keyType, sizeof(keyType) };
        CK_ATTRIBUTE aMechType = { CKA_KEY_GEN_MECHANISM, &mechType, sizeof(mechType) };
        CK_ATTRIBUTE aLocal = { CKA_LOCAL, &ckTrue, sizeof(ckTrue) };
        
        CK_ATTRIBUTE aLabel = { CKA_LABEL, NULL_PTR, 0 };
        CK_ATTRIBUTE aId = { CKA_ID, NULL_PTR, 0 };
        CK_ATTRIBUTE aSubject = { CKA_SUBJECT, NULL_PTR, 0 };
        CK_ATTRIBUTE aPrivate = { CKA_PRIVATE, &ckTrue, sizeof(ckTrue) };
        CK_ATTRIBUTE aModifiable = { CKA_MODIFIABLE, &ckTrue, sizeof(ckTrue) };
        CK_ATTRIBUTE aToken = { CKA_TOKEN, &ckFalse, sizeof(ckFalse) };
        CK_ATTRIBUTE aDerive = { CKA_DERIVE, &ckFalse, sizeof(ckFalse) };
        CK_ATTRIBUTE aEncrypt = { CKA_ENCRYPT, &ckTrue, sizeof(ckTrue) };
        CK_ATTRIBUTE aVerify = { CKA_VERIFY, &ckTrue, sizeof(ckTrue) };
        CK_ATTRIBUTE aVerifyRecover = { CKA_VERIFY_RECOVER, &ckTrue, sizeof(ckTrue) };
        CK_ATTRIBUTE aWrap = { CKA_WRAP, &ckTrue, sizeof(ckTrue) };
        CK_ATTRIBUTE aTrusted = { CKA_TRUSTED, &ckFalse, sizeof(ckFalse) };
        CK_ATTRIBUTE aStartDate = { CKA_START_DATE, &emptyDate, 0 };
        CK_ATTRIBUTE aEndDate = { CKA_END_DATE, &emptyDate, 0 };
        
        // NOTE: CKA_VENDOR_DEFINED = CKA_RABBIT_HANDLER :D.
        CK_ATTRIBUTE aValue = { CKA_VENDOR_DEFINED, &handler, sizeof(handler) };
        
        // Se sobre escriben los datos...
        for(CK_ULONG i = 0; i < ulPublicKeyAttributeCount; i++) {
          switch(pPublicKeyTemplate[i].type) {
            case CKA_LABEL:
              aLabel = { pPublicKeyTemplate[i].type, pPublicKeyTemplate[i].pValue, pPublicKeyTemplate[i].ulValueLen };
              break;
            case CKA_ID:
              aId = { pPublicKeyTemplate[i].type, pPublicKeyTemplate[i].pValue, pPublicKeyTemplate[i].ulValueLen };
              break;
            case CKA_SUBJECT:
              aSubject = { pPublicKeyTemplate[i].type, pPublicKeyTemplate[i].pValue, pPublicKeyTemplate[i].ulValueLen };
              break;
            case CKA_DERIVE:
              aDerive = { pPublicKeyTemplate[i].type, pPublicKeyTemplate[i].pValue, pPublicKeyTemplate[i].ulValueLen };
              break;
            case CKA_TOKEN:
              aToken = { pPublicKeyTemplate[i].type, pPublicKeyTemplate[i].pValue, pPublicKeyTemplate[i].ulValueLen };
              break;
            case CKA_PRIVATE:
              aPrivate = { pPublicKeyTemplate[i].type, pPublicKeyTemplate[i].pValue, pPublicKeyTemplate[i].ulValueLen };
              break;
            case CKA_MODIFIABLE:
              aModifiable = { pPublicKeyTemplate[i].type, pPublicKeyTemplate[i].pValue, pPublicKeyTemplate[i].ulValueLen };
              break;
            case CKA_ENCRYPT:
              aEncrypt = { pPublicKeyTemplate[i].type, pPublicKeyTemplate[i].pValue, pPublicKeyTemplate[i].ulValueLen };
              break;
            case CKA_VERIFY:
              aVerify = { pPublicKeyTemplate[i].type, pPublicKeyTemplate[i].pValue, pPublicKeyTemplate[i].ulValueLen };
              break;
            case CKA_VERIFY_RECOVER:
              aVerifyRecover = { pPublicKeyTemplate[i].type, pPublicKeyTemplate[i].pValue, pPublicKeyTemplate[i].ulValueLen };
              break;
            case CKA_WRAP:
              aWrap = { pPublicKeyTemplate[i].type, pPublicKeyTemplate[i].pValue, pPublicKeyTemplate[i].ulValueLen };
              break;
            case CKA_TRUSTED:
              aSubject = { pPublicKeyTemplate[i].type, pPublicKeyTemplate[i].pValue, pPublicKeyTemplate[i].ulValueLen };
              break;
            case CKA_START_DATE:
              aStartDate = { pPublicKeyTemplate[i].type, pPublicKeyTemplate[i].pValue, pPublicKeyTemplate[i].ulValueLen };
              break;
            case CKA_END_DATE:
              aEndDate = { pPublicKeyTemplate[i].type, pPublicKeyTemplate[i].pValue, pPublicKeyTemplate[i].ulValueLen };
              break;
            default:
              break;
          }
        }
                
        CK_ATTRIBUTE attributes[] = {
          aClass,
          aKeyType,
          aMechType,
          aLocal,
          aLabel,
          aId,
          aSubject,
          aPrivate,
          aModifiable,
          aToken,
          aDerive,
          aEncrypt,
          aVerify,
          aVerifyRecover,
          aWrap,
          aTrusted,
          aStartDate,
          aEndDate,
          aValue
        };
          
        return createObject(attributes, sizeof(attributes)/sizeof(attributes[0]));
        
      } catch (std::exception & e) {
        throw TcbError("Session::generateKeyPair", e.what(), CKR_GENERAL_ERROR);
      }
      break;
    default:
      break;
  }

  throw TcbError("Session::generateKeyPair", "Mecanismo no soportado", CKR_MECHANISM_INVALID);
  
}

