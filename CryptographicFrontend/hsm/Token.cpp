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

#include <cstring>

#include "Token.h"
#include "Slot.h"
#include "CryptoObject.h"
#include "TcbError.h"


using namespace hsm;

Token::Token ( std::string label, std::string userPin, std::string soPin )
    : userPin_ ( userPin ), soPin_ ( soPin )
    , securityLevel_ ( SecurityLevel::PUBLIC ), loggedIn_ ( false )
{
    if ( label.size() <= 32 ) {
        label_ = label;
    } else {
        throw TcbError ( "Token::Token", "Etiqueta con mas de 32 caracteres", CKR_ARGUMENTS_BAD );
    }

    // TODO: Deserialize Token Objects
}

Token::~Token()
{
}

void Token::getInfo ( CK_TOKEN_INFO_PTR pInfo ) const
{
    if ( !pInfo ) {
        throw TcbError ( "Token::getInfo", "pInfo == nullptr", CKR_ARGUMENTS_BAD );
    }

    if ( label_.empty() ) {
        memset ( pInfo->label, ' ', 32 );
    } else {
        char label[32];
        memset ( label, ' ', 32 );
        memcpy ( label, label_.c_str(), label_.size() );
        memcpy ( pInfo->label, label, 32 );
    }

    memset ( pInfo->manufacturerID, ' ', 32 );
    memset ( pInfo->model, ' ', 16 );
    memset ( pInfo->serialNumber, ' ', 16 );

    memcpy ( pInfo->manufacturerID, "NicLabs", 7 );
    memcpy ( pInfo->model, "tcbhsm", 6 );
    memcpy ( pInfo->serialNumber, "1", 1 );

    pInfo->flags = tokenFlags_;
    pInfo->ulMaxSessionCount = MAX_SESSION_COUNT;
    pInfo->ulSessionCount = CK_UNAVAILABLE_INFORMATION;
    pInfo->ulMaxRwSessionCount = MAX_SESSION_COUNT;
    pInfo->ulRwSessionCount = CK_UNAVAILABLE_INFORMATION;
    pInfo->ulMaxPinLen = MAX_PIN_LEN;
    pInfo->ulMinPinLen = MIN_PIN_LEN;
    pInfo->ulTotalPublicMemory = CK_UNAVAILABLE_INFORMATION;
    pInfo->ulFreePublicMemory = CK_UNAVAILABLE_INFORMATION;
    pInfo->ulTotalPrivateMemory = CK_UNAVAILABLE_INFORMATION;
    pInfo->ulFreePrivateMemory = CK_UNAVAILABLE_INFORMATION;
    pInfo->hardwareVersion.major = VERSION_MAJOR;
    pInfo->hardwareVersion.minor = VERSION_MINOR;
    pInfo->firmwareVersion.major = VERSION_MAJOR;
    pInfo->firmwareVersion.minor = VERSION_MINOR;

    time_t rawtime;
    time ( &rawtime );
    char dateTime[17];
    strftime ( dateTime, 17, "%Y%m%d%H%M%S00", gmtime ( &rawtime ) );
    memcpy ( pInfo->utcTime, dateTime, 16 );
}

void Token::setUserPin ( std::string pin )
{
    userPin_ = pin;
}

bool Token::isInited() const
{
    return true;
}

auto Token::getSecurityLevel() const -> SecurityLevel
{
    return securityLevel_;
}

Token::SecurityLevel Token::checkUserPin ( CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen ) const
{
    std::string pin ( reinterpret_cast<char *> ( pPin ), ulPinLen );
    if ( userPin_ == pin ) {
        return SecurityLevel::USER;
    } else {
        throw TcbError ( "Token::login", "Mal pin", CKR_PIN_INCORRECT );
    }
}

Token::SecurityLevel Token::checkSecurityOfficerPin ( CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen ) const
{
    std::string pin ( reinterpret_cast<char *> ( pPin ), ulPinLen );
    if ( soPin_ == pin ) {
        return SecurityLevel::SECURITY_OFFICER;
    } else {
        throw TcbError ( "Token::login", "Mal pin", CKR_PIN_INCORRECT );
    }
}

void Token::login ( CK_USER_TYPE userType, CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen )
{
    if ( loggedIn_
            &&
            ( ( userType == CKU_USER && securityLevel_ == SecurityLevel::SECURITY_OFFICER )
              ||
              ( userType == CKU_SO && securityLevel_ == SecurityLevel::USER ) ) ) {
        throw TcbError ( "Token::login",
                         "loggedIn_ == true",
                         CKR_USER_ANOTHER_ALREADY_LOGGED_IN );

    }

    if ( pPin == nullptr ) {
        throw TcbError ( "Token::login", "pPin == nullptr", CKR_ARGUMENTS_BAD );
    }

    switch ( userType ) {
    case CKU_SO:
        securityLevel_ = checkSecurityOfficerPin ( pPin, ulPinLen );
        break;
    case CKU_USER:
        securityLevel_ = checkUserPin ( pPin, ulPinLen );
        break;
    case CKU_CONTEXT_SPECIFIC:
        switch ( securityLevel_ ) {
        case SecurityLevel::PUBLIC:
            throw TcbError ( "Token::login", "Mal userType", CKR_OPERATION_NOT_INITIALIZED );

        case SecurityLevel::USER:
            securityLevel_ = checkUserPin ( pPin, ulPinLen );
            break;

        case SecurityLevel::SECURITY_OFFICER:
            securityLevel_ = checkSecurityOfficerPin ( pPin, ulPinLen );
            break;
        }
        break;
    default:
        throw TcbError ( "Token::login", "Mal userType", CKR_USER_TYPE_INVALID );
        break;
    }

    loggedIn_ = true;
}

void Token::logout()
{
    securityLevel_ = SecurityLevel::PUBLIC;
    loggedIn_ = false;
}

CK_OBJECT_HANDLE Token::addObject ( CryptoObject * object )
{
    CK_OBJECT_HANDLE handle = object->getHandle();
    ( objects_[handle] ).reset ( object );
    return handle;
}

std::string Token::getLabel() const
{
    return label_;
}

CryptoObject & Token::getObject ( CK_OBJECT_HANDLE handle )
{
    return * ( objects_.at ( handle ) );
}

std::map<CK_OBJECT_HANDLE, CryptoObjectPtr> & Token::getObjects()
{
    return objects_;
}
