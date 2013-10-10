/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef SESSION_H
#define SESSION_H

#include "config.h"

#include "cf/Connection.hpp"
#include "cryptoki.h"

#include "Configuration.h"
#include "Slot.h"

#include <botan/pipe.h>
#include <botan/auto_rng.h>

#include <memory>
#include <utility>

namespace tcbhsm
{

using KeyPair = std::pair<CK_OBJECT_HANDLE, CK_OBJECT_HANDLE>; // (Private, Public)
using DigestPipePtr = std::unique_ptr<Botan::Pipe>;

// Sessions are enclosed in operations with objects, Tokens on containing objects.
class Session
{
public:
  Session(CK_FLAGS flags, CK_VOID_PTR pApplication, 
          CK_NOTIFY notify, Slot & currentSlot, 
          Configuration const & configuration);
  
  ~Session();

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

  // Conexiones
  cf::ConnectionPtr createConnection(); // RAII connection in a std::unique_ptr

  CK_OBJECT_HANDLE createObject(CK_ATTRIBUTE_PTR pTemplate, 
                                CK_ULONG ulCount); // throws exception
  void destroyObject(CK_OBJECT_HANDLE hObject); // throws exception
  void findObjectsInit(CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount);
  std::vector<CK_OBJECT_HANDLE> findObjects(CK_ULONG maxObjectCount);
  void findObjectsFinal();
  CryptoObject & getObject(CK_OBJECT_HANDLE objectHandle); // throws exception

private:
  const CK_FLAGS flags_;
  const CK_VOID_PTR application_;
  const CK_NOTIFY notify_;
  Slot & currentSlot_;
  
  // Configuration
  Configuration const & configuration_;
  
  // Object Search
  bool findInitialized = false;
  std::vector<CK_OBJECT_HANDLE> foundObjects;
  std::vector<CK_OBJECT_HANDLE>::iterator foundObjectsIterator;
  std::vector<CK_OBJECT_HANDLE>::iterator foundObjectsEnd;
  
  // Signing
  bool signInitialized_ = false;
  
  // Digest 
  bool digestInitialized_ = false;
  DigestPipePtr digestPipe_;
  CK_ULONG digestSize_;
  
  // Random Number Generation
  Botan::AutoSeeded_RNG rng;
};

using SessionPtr = std::unique_ptr<Session>;
}

#endif // SESSION_H
