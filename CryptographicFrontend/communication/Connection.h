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

#ifndef COMMUNICATION_CONNECTION_H
#define COMMUNICATION_CONNECTION_H

#include <string>
#include <memory>
#include <exception>

namespace communication
{

struct ConnectionException : std::exception {};

class Connection
{

protected:
    virtual void send ( const std::string &message ) const = 0;
    virtual std::string receive() const = 0;
public:
    class BadResponseException : public ConnectionException
    {
        const char *what() const throw() {
            return "Error al recibir respuesta";
        }
    };

    class CannotConnectException : public ConnectionException
    {
        const char *what() const throw() {
            return "No se puede conectar";
        }
    };

    virtual ~Connection() = default;

    // Template method...
    std::string executeRpc ( const std::string &message ) const { // throw (ConnectionException)
        send ( message );
        return receive();
    }
};

}

#endif // Connection_H_
// kate: indent-mode cstyle; replace-tabs on; 
