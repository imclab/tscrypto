/*
Copyright 2013 NIC Chile Research Labs.
This file is part of PKCS11-TsCrypto.

PKCS11-TsCrypto is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

PKCS11-TsCrypto is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with PKCS11-TsCrypto.  If not, see <http://www.gnu.org/licenses/>.
*/

#ifndef TCBHSM_SESSION_H
#define TCBHSM_SESSION_H

#include "config.h"

#include "pkcs11.h"

#include <memory>
#include <vector>
#include <utility>

namespace hsm
{

using KeyPair = std::pair<CK_OBJECT_HANDLE, CK_OBJECT_HANDLE>; // (Private, Public)

class Configuration;
class Slot;
class CryptoObject;

// Sessions are enclosed in operations with objects, Tokens on containing objects.
class Session
{
    // Parent..
    Slot & slot_;    
    
    std::string uuid_;
    const CK_SESSION_HANDLE handle_;
    const CK_FLAGS flags_;
    
    // Future use
    const CK_VOID_PTR application_;
    const CK_NOTIFY notify_;

    // Object Search
    bool findInitialized_ = false;
    std::vector<CK_OBJECT_HANDLE> foundObjects_;
    std::vector<CK_OBJECT_HANDLE>::iterator foundObjectsIterator_;
    std::vector<CK_OBJECT_HANDLE>::iterator foundObjectsEnd_;

    // Signing
    bool signInitialized_ = false;

    // Digest
    bool digestInitialized_ = false;
    CK_ULONG digestSize_;    
    
public:
    Session ( CK_FLAGS flags, CK_VOID_PTR pApplication,
              CK_NOTIFY notify, Slot & currentSlot );

    virtual ~Session();

    virtual CK_SESSION_HANDLE getHandle() const;
    virtual std::string const & getUuid();
    virtual CK_STATE getState() const;
    virtual CK_FLAGS getFlags() const;

    virtual void getSessionInfo ( CK_SESSION_INFO_PTR pInfo ) const;
    virtual bool isReadOnly() const;
    virtual Slot & getCurrentSlot();

    virtual void login ( CK_USER_TYPE userType, CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen );
    virtual void logout();

    // Cryptographic functions
    virtual KeyPair generateKeyPair ( CK_MECHANISM_PTR pMechanism,
                                      CK_ATTRIBUTE_PTR pPublicKeyTemplate,
                                      CK_ULONG ulPublicKeyAttributeCount,
                                      CK_ATTRIBUTE_PTR pPrivateKeyTemplate,
                                      CK_ULONG ulPrivateKeyAttributeCount );
    virtual void signInit ( CK_MECHANISM_PTR pMechanism, CK_OBJECT_HANDLE hKey );
    virtual void sign ( CK_BYTE_PTR pData, CK_ULONG ulDataLen, CK_BYTE_PTR pSignature,
                CK_ULONG_PTR pulSignatureLen );
    virtual void digestInit ( CK_MECHANISM_PTR pMechanism );
    virtual void digest ( CK_BYTE_PTR pData, CK_ULONG ulDataLen, CK_BYTE_PTR pDigest,
                  CK_ULONG_PTR pulDigestLen );
    virtual void seedRandom ( CK_BYTE_PTR pSeed, CK_ULONG ulSeedLen );
    virtual void generateRandom ( CK_BYTE_PTR pRandomData, CK_ULONG ulRandomLen );

    virtual CK_OBJECT_HANDLE createObject ( CK_ATTRIBUTE_PTR pTemplate,
                                    CK_ULONG ulCount ); // throws exception
    virtual void destroyObject ( CK_OBJECT_HANDLE hObject ); // throws exception
    virtual void findObjectsInit ( CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount );
    virtual std::vector<CK_OBJECT_HANDLE> findObjects ( CK_ULONG maxObjectCount );
    virtual void findObjectsFinal();
    virtual CryptoObject & getObject ( CK_OBJECT_HANDLE objectHandle ); // throws exception
};

}

#endif // TCBHSM_SESSION_H
