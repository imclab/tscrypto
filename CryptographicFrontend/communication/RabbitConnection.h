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

#ifndef COMMUNICATION_RABBITCONNECTION_H
#define COMMUNICATION_RABBITCONNECTION_H
#include "Connection.h"
#include <amqp.h>

namespace communication
{

class RabbitConnection : public Connection
{
private:
    int sockFd_;
    amqp_connection_state_t connection_;
    amqp_channel_t channel_;
    amqp_bytes_t replyToQueue_;
    std::string exchange_;
    std::string routingKey_;

protected:
    RabbitConnection ( RabbitConnection const & rhs ) = delete;    
    RabbitConnection & operator= ( RabbitConnection const & rhs ) = delete;
    virtual void send ( const std::string & message ) const;
    virtual std::string receive() const;

public:
    RabbitConnection();
    RabbitConnection ( const std::string & host, int port, const std::string & exchange,
                       const std::string & routingKey, amqp_channel_t channel );
    RabbitConnection ( RabbitConnection && other );
    RabbitConnection& operator= ( RabbitConnection && rhs );
    virtual ~RabbitConnection();

};

}

#endif // RabbitConnection_H_
// kate: indent-mode cstyle; replace-tabs on; 
