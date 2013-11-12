/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "Slot.h"
#include "Session.h"
#include "Token.h"
#include "TcbError.h"
#include "Configuration.h"
#include "ConnectionManager.h"
#include "Application.h"

#include <communication/Connection.hpp>
#include <communication/OpenSessionMethod.hpp>
#include <communication/CloseSessionMethod.hpp>
#include <communication/ResponseMessage.hpp>

using namespace hsm;

Slot::Slot ( CK_SLOT_ID id, Application& application )
    : slotId_ ( id ), application_ ( application )
{

}

Slot::~Slot()
{

}

CK_SESSION_HANDLE
Slot::openSession ( CK_FLAGS flags, CK_VOID_PTR pApplication, CK_NOTIFY notify )
{
    if ( !isTokenPresent() ) {
        throw TcbError ( "Slot::openSession",
                         "Token not present",
                         CKR_TOKEN_NOT_PRESENT );
    }

    Session * sessionPtr = new Session ( flags, pApplication, notify, *this );

    CK_SESSION_HANDLE handle = sessionPtr->getHandle();
    sessions_[handle].reset ( sessionPtr );

    return handle;
}

void Slot::closeSession ( CK_SESSION_HANDLE handle )
{
    if ( !isTokenPresent() ) {
        throw TcbError ( "Slot::openSession",
                         "Token not present",
                         CKR_TOKEN_NOT_PRESENT );
    }

    if ( sessions_.erase ( handle ) == 0 ) {
        throw TcbError ( "Slot::closeSession",
                         "Session handle doesn't exists in this slot",
                         CKR_SESSION_HANDLE_INVALID );
    }
}

void Slot::closeAllSessions()
{
    if ( !isTokenPresent() ) {
        throw TcbError ( "Slot::openSession",
                         "Token not present",
                         CKR_TOKEN_NOT_PRESENT );
    }

    sessions_.clear();
}

Session & Slot::getSession ( CK_SESSION_HANDLE handle )
{
    if ( !isTokenPresent() ) {
        throw TcbError ( "Slot::openSession",
                         "Token not present",
                         CKR_TOKEN_NOT_PRESENT );
    }

    try {
        return * ( sessions_.at ( handle ) );
    } catch ( ... ) {
        throw TcbError ( "Slot::getSession",
                         "Session handle doesn't exists in this slot",
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

const Application& Slot::getApplication() const
{
    return application_;
}
