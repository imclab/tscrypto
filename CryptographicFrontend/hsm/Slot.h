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

#ifndef TCBHSM_SLOT_H
#define TCBHSM_SLOT_H

#include "config.h"
#include <string>
#include <map>
#include <memory>

#include "pkcs11.h"
#include "Configuration.h"

namespace hsm
{

class Application;

class ConnectionManager;

class Token;
class Session;
class Configuration;

using TokenPtr = std::unique_ptr<Token>;
using SessionPtr = std::unique_ptr<Session>;

/// Clase que representa un espacio para un token en el HSM
class Slot
{
    CK_SLOT_ID slotId_;
    Application const & application_;
    CK_FLAGS slotFlags_;
    
    std::map<CK_SESSION_HANDLE, SessionPtr> sessions_;
    // A token can be unplugged...
    TokenPtr token_;
    
    CK_SLOT_ID id_;
public:
    Slot ( CK_SLOT_ID id, Application& application );
    Slot ( Slot & ) = delete;
    Slot (Slot && ) = default;
    Slot & operator=(Slot &) = delete;
    Slot & operator=(Slot &&) = default;
    virtual ~Slot() = default;

    virtual CK_SESSION_HANDLE openSession ( CK_FLAGS flags, CK_VOID_PTR pApplication, CK_NOTIFY notify );
    virtual void closeSession ( CK_SESSION_HANDLE handle );
    virtual void closeAllSessions();
    virtual Session & getSession ( CK_SESSION_HANDLE handle );
    virtual bool hasSession ( CK_SESSION_HANDLE handle );
    virtual CK_ULONG sessionsCount() const;

    virtual Application const & getApplication() const;
    virtual CK_SLOT_ID getId() const;
    virtual void getInfo ( CK_SLOT_INFO_PTR pInfo ) const; // throws exception
    virtual void insertToken ( Token * token );
    virtual Token & getToken() const; // throws exception
    virtual bool isTokenPresent() const;
};
}

#endif // TCBHSM_SLOT_H
