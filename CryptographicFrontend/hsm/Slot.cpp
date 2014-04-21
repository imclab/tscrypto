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

#include <Connection.h>
#include <ResponseMessage.h>

#include "Slot.h"
#include "Session.h"
#include "Token.h"
#include "TcbError.h"
#include "Configuration.h"
#include "ConnectionManager.h"
#include "Application.h"


using namespace hsm;

Slot::Slot ( CK_SLOT_ID id, Application& application )
    : slotId_ ( id ), application_ ( application )
{
}

CK_SESSION_HANDLE
Slot::openSession ( CK_FLAGS flags, CK_VOID_PTR pApplication, CK_NOTIFY notify )
{
    if ( !isTokenPresent() ) {
        throw TcbError ( "Slot::openSession", "Token not present", CKR_TOKEN_NOT_PRESENT );
    }

    Session * sessionPtr = new Session ( flags, pApplication, notify, *this );

    CK_SESSION_HANDLE handle = sessionPtr->getHandle();
    sessions_[handle].reset ( sessionPtr );

    return handle;
}

void Slot::closeSession ( CK_SESSION_HANDLE handle )
{
    if ( !isTokenPresent() ) {
        throw TcbError ( "Slot::openSession", "Token not present", CKR_TOKEN_NOT_PRESENT );
    }

    if ( sessions_.erase ( handle ) == 0 ) {
        throw TcbError ( "Slot::closeSession", "Session handle doesn't exists in this slot",
                         CKR_SESSION_HANDLE_INVALID );
    }
}

void Slot::closeAllSessions()
{
    if ( !isTokenPresent() ) {
        throw TcbError ( "Slot::openSession", "Token not present", CKR_TOKEN_NOT_PRESENT );
    }

    sessions_.clear();
}

Session & Slot::getSession ( CK_SESSION_HANDLE handle )
{
    if ( !isTokenPresent() ) {
        throw TcbError ( "Slot::openSession", "Token not present", CKR_TOKEN_NOT_PRESENT );
    }

    try {
        return * ( sessions_.at ( handle ) );
    } catch ( ... ) {
        throw TcbError ( "Slot::getSession", "Session handle doesn't exists in this slot",
                         CKR_SESSION_HANDLE_INVALID );
    }
}

bool Slot::hasSession ( CK_SESSION_HANDLE handle )
{
    return sessions_.count ( handle ) > 0;
}

CK_ULONG Slot::sessionsCount() const
{
    return sessions_.size();
}

CK_SLOT_ID Slot::getId() const
{
    return slotId_;
}

void Slot::getInfo ( CK_SLOT_INFO_PTR pInfo ) const
{
    if ( pInfo == NULL ) {
        throw TcbError ( "El puntero pasado a getInfo es NULL.", CKR_ARGUMENTS_BAD );
    }

    std::fill ( pInfo->slotDescription, pInfo->slotDescription + 64, ' ' );
    std::fill ( pInfo->manufacturerID, pInfo->manufacturerID + 32, ' ' );

    char slotDescription[] = "Slot de TCBHSM";
    char manufacturerID[] = "NicLabs";

    std::copy ( slotDescription, slotDescription + 14, pInfo->slotDescription );
    std::copy ( manufacturerID, manufacturerID + 7, pInfo->manufacturerID );

    pInfo->flags = slotFlags_;

    pInfo->hardwareVersion.major = VERSION_MAJOR;
    pInfo->hardwareVersion.minor = VERSION_MINOR;
    pInfo->firmwareVersion.major = VERSION_MAJOR;
    pInfo->firmwareVersion.minor = VERSION_MINOR;
}

bool Slot::isTokenPresent() const
{
    return token_ != nullptr;
}

Token & Slot::getToken() const
{
    if ( isTokenPresent() ) {
        return *token_;
    } else {
        throw TcbError ( "El token no esta presente.", CKR_TOKEN_NOT_PRESENT );
    }
}

void Slot::insertToken ( Token * token )
{
    token_.reset ( token );
}

Application& Slot::getApplication()
{
    return application_;
}
