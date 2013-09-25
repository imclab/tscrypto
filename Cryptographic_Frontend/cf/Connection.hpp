/**
 * @file
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef Connection_H_
#define Connection_H_

#include <string>
#include <memory>
#include <exception>

namespace cf
{

class ConnectionException : public std::exception {};

class Connection
{

protected:
    virtual void send(const std::string &message) const = 0;
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
    std::string executeRpc(const std::string &message) const { // throw (ConnectionException)
        send(message);
        return receive();
    }
};

using ConnectionPtr = std::unique_ptr<cf::Connection>;

}

#endif // Connection_H_
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
