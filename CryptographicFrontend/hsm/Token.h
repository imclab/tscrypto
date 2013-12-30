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

#ifndef TCBHSM_TOKEN_H
#define TCBHSM_TOKEN_H

#include "config.h"

#include <map>
#include <string>
#include <memory>

#include "pkcs11.h"

namespace hsm
{

class Slot;
class Session;
class Slot;
class CryptoObject;
using CryptoObjectPtr = std::unique_ptr<CryptoObject>;
// Tokens are (unlimited) containers of crypto objects...
class Token
{
public:
    enum class SecurityLevel
    {
        SECURITY_OFFICER, USER, PUBLIC
    };

    Token ( std::string label, std::string userPin, std::string soPin);
    virtual ~Token();

    // Basic operations      
    virtual void getInfo ( CK_TOKEN_INFO_PTR pInfo ) const;
    virtual void setUserPin ( std::string pin );
    virtual bool isInited() const;
    virtual void login ( CK_USER_TYPE userType, CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen );
    virtual void logout();
    virtual SecurityLevel getSecurityLevel() const;
    virtual std::string getLabel() const;

    // Container operations
    virtual CK_OBJECT_HANDLE addObject ( CryptoObject * object );
    virtual CryptoObject & getObject ( CK_OBJECT_HANDLE handle );
    virtual std::map<CK_OBJECT_HANDLE, CryptoObjectPtr> & getObjects();    

protected:
    SecurityLevel checkUserPin ( CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen ) const;
    SecurityLevel checkSecurityOfficerPin ( CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen ) const;
    
private:
    CK_FLAGS tokenFlags_;
    std::string userPin_;
    std::string soPin_;
    std::string label_;
    SecurityLevel securityLevel_;
    bool loggedIn_;
//     int sessionCount_; // TODO
//     int rwSessionCount; // TODO

    std::map<CK_OBJECT_HANDLE, CryptoObjectPtr> objects_;
};

}


#endif // TCBHSM_TOKEN_H
