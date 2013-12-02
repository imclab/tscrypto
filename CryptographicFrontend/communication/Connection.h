/**
 * @file
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef COMMUNICATION_CONNECTION_H
#define COMMUNICATION_CONNECTION_H

#include <string>
#include <memory>
#include <exception>

namespace communication
{

class ConnectionException : public std::exception {};

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
