/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef SESSION_H
#define SESSION_H

#include "config.h"
#include "cf/Connection.hpp"
#include "cryptoki.h"

#include <map>
#include <memory>
#include "SessionObject.h"
#include "Slot.h"
namespace tcbhsm
{
using SessionObjectPtr = std::unique_ptr<SessionObject>;
using ConnectionPtr = std::unique_ptr<cf::Connection>;

// Una sesion es ademas un container de objetos de session.
class Session
{
public:
  Session(CK_FLAGS flags, CK_VOID_PTR pApplication, CK_NOTIFY notify, Slot & currentSlot);

  CK_STATE getState() const;
  CK_FLAGS getFlags() const;

  void getSessionInfo(CK_SESSION_INFO_PTR pInfo) const;
  bool isReadOnly() const;

  void login(CK_USER_TYPE userType, CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen);
  void logout();

  // Funciones criptográficas
  CK_OBJECT_HANDLE generateKeyPair(CK_MECHANISM_PTR pMechanism, 
                                   CK_ATTRIBUTE_PTR pPublicKeyTemplate, CK_ULONG ulPublicKeyAttributeCount, 
                                   CK_ATTRIBUTE_PTR pPrivateKeyTemplate, CK_ULONG ulPrivateKeyAttributeCount);
    

  // Conexiones
  ConnectionPtr && createConnection(); // RAII connection encapsulada en un puntero. R-Value reference.

  // Session Objects
  CK_OBJECT_HANDLE createObject(CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount); // throws exception
  void destroyObject(CK_OBJECT_HANDLE hObject); // throws exception
  void findObjectsInit(CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount);
  std::vector<CK_OBJECT_HANDLE> findObjects(CK_ULONG maxObjectCount);
  SessionObject & getObject(CK_OBJECT_HANDLE objectHandle); // throws exception

  // Reference counting;
  void retain();
  void release();
  unsigned int count();

private:
  CK_OBJECT_HANDLE actualObjectHandle_;
  std::map<CK_OBJECT_HANDLE, SessionObjectPtr> objects_;
  unsigned int refCount_;
  const CK_FLAGS flags_;
  const CK_VOID_PTR application_;
  const CK_NOTIFY notify_;
  Slot & currentSlot_;

  // Busqueda de Objetos
  bool findInitialized = false;
  std::vector<CK_OBJECT_HANDLE> foundObjects;
  std::vector<CK_OBJECT_HANDLE>::iterator foundObjectsIterator;
  std::vector<CK_OBJECT_HANDLE>::iterator foundObjectsEnd;

};
}

#endif // SESSION_H
