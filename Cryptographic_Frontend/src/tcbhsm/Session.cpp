/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */


#include <cstdlib> // getenv
#include "tcbhsm/Session.h"
#include "cf/RabbitConnection.hpp"
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
