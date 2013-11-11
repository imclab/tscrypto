/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef TCBHSM_SESSION_H
#define TCBHSM_SESSION_H

#include "config.h"

#include "cryptoki.h"

#include <memory>
#include <vector>
#include <utility>

namespace communication {
  class Connection;
}

namespace hsm
{

using KeyPair = std::pair<CK_OBJECT_HANDLE, CK_OBJECT_HANDLE>; // (Private, Public)

class Configuration;
class Slot;
class CryptoObject;

// Sessions are enclosed in operations with objects, Tokens on containing objects.
class Session
{
public:
  Session(CK_FLAGS flags, CK_VOID_PTR pApplication, 
          CK_NOTIFY notify, Slot & currentSlot);
  
  ~Session();

  CK_SESSION_HANDLE getHandle() const;
  std::string const & getUuid();
  CK_STATE getState() const;
  CK_FLAGS getFlags() const;

  void getSessionInfo(CK_SESSION_INFO_PTR pInfo) const;
  bool isReadOnly() const;
  Slot & getCurrentSlot();

  void login(CK_USER_TYPE userType, CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen);
  void logout();

  // Cryptographic functions
  KeyPair generateKeyPair(CK_MECHANISM_PTR pMechanism,
                          CK_ATTRIBUTE_PTR pPublicKeyTemplate,
                          CK_ULONG ulPublicKeyAttributeCount,
                          CK_ATTRIBUTE_PTR pPrivateKeyTemplate,
                          CK_ULONG ulPrivateKeyAttributeCount);
  void signInit(CK_MECHANISM_PTR pMechanism, CK_OBJECT_HANDLE hKey);
  void sign(CK_BYTE_PTR pData, CK_ULONG ulDataLen, CK_BYTE_PTR pSignature, 
            CK_ULONG_PTR pulSignatureLen);
  void digestInit(CK_MECHANISM_PTR pMechanism);
  void digest(CK_BYTE_PTR pData, CK_ULONG ulDataLen, CK_BYTE_PTR pDigest, 
              CK_ULONG_PTR pulDigestLen);
  void seedRandom(CK_BYTE_PTR pSeed, CK_ULONG ulSeedLen);
  void generateRandom(CK_BYTE_PTR pRandomData, CK_ULONG ulRandomLen);

  CK_OBJECT_HANDLE createObject(CK_ATTRIBUTE_PTR pTemplate, 
                                CK_ULONG ulCount); // throws exception
  void destroyObject(CK_OBJECT_HANDLE hObject); // throws exception
  void findObjectsInit(CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount);
  std::vector<CK_OBJECT_HANDLE> findObjects(CK_ULONG maxObjectCount);
  void findObjectsFinal();
  CryptoObject & getObject(CK_OBJECT_HANDLE objectHandle); // throws exception

private:
  communication::Connection* getConnection() const;
  std::string uuid_;
  const CK_SESSION_HANDLE handle_;
  const CK_FLAGS flags_;
  const CK_VOID_PTR application_;
  const CK_NOTIFY notify_;
  Slot & slot_;
  
  // Object Search
  bool findInitialized = false;
  std::vector<CK_OBJECT_HANDLE> foundObjects;
  std::vector<CK_OBJECT_HANDLE>::iterator foundObjectsIterator;
  std::vector<CK_OBJECT_HANDLE>::iterator foundObjectsEnd;
  
  // Signing
  bool signInitialized_ = false;
  
  // Digest 
  bool digestInitialized_ = false;
  CK_ULONG digestSize_; 
};

}

#endif // TCBHSM_SESSION_H
