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

#include "Application.h"
#include "Configuration.h"
#include "CryptoObject.h"
#include "Session.h"
#include "Slot.h"
#include "Token.h"
#include "TcbError.h"
#include "RabbitConnectionManager.h"
#include "TokenSerializer.h"

#include <base64.h>

#include <cstdlib> // getenv
#include <ostream>
#include <iostream>

using namespace hsm;


Application::Application ( std::ostream& out )
    : out_ ( out )
{
    // First, read and setup the configuration.
    char const * configPath = std::getenv ( "TCB_CONFIG_FILE" );
    if ( configPath == nullptr ) {
        throw TcbError ( "Application::Application",
                         "TCB_CONFIG_FILE environment variable hasn't setted yet",
                         CKR_DEVICE_ERROR );
    }
    
    configuration_.load( configPath );
    connectionManager_.init ( configuration_.getRabbitMqConf() );
    database_.init(configuration_.getDatabaseConf());
    
    // By design, we will have one slot per configured token.
    // The tokens are owned by the slots.
    CK_SLOT_ID i = 0;
    for ( Configuration::SlotConf const & slotConf: configuration_.getSlotConf() ) {
	Slot * slot = new Slot(i, *this);
	
	slot->insertToken(database_.getToken(slotConf.label));
	
        slots_.push_back ( SlotPtr(slot) );
	++i;
    }


}

Application::~Application()
{
    for ( auto const& slotPtr: slots_ ) {
        database_.saveToken ( slotPtr->getToken() );
    }
}

void Application::errorLog ( std::string message ) const
{
    out_ << message << std::endl;
}

Session & Application::getSession ( CK_SESSION_HANDLE session )
{
    return getSessionSlot ( session ).getSession ( session );
}

const std::vector<SlotPtr> & Application::getSlotList() const
{
    return slots_;
}

Slot & Application::getSlot ( CK_SLOT_ID id ) const
{
    unsigned int i = static_cast<unsigned int> ( id );
    try {
        Slot &slot = * ( slots_.at ( i ) );
        return slot;
    } catch ( std::out_of_range &e ) {
        throw TcbError ( "Application::getSlot", e.what(), CKR_SLOT_ID_INVALID );
    }
}

Slot & Application::getSessionSlot ( CK_SESSION_HANDLE handle )
{
    for ( auto & slotPtr: slots_ ) {
        if ( slotPtr->hasSession ( handle ) ) {
            return *slotPtr;
        }
    }

    throw TcbError ( "Application::getSessionSlot",
                     "Session not found.",
                     CKR_SESSION_HANDLE_INVALID );
}

ConnectionManager const & Application::getConnectionManager() const
{
    return connectionManager_;
}
