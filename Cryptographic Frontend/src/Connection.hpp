/**
 * @file
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef Connection_H_
#define Connection_H_

#include <string>
#include <exception>

namespace cf
{
    
    class ConnectionException : public std::exception {};
    
    class Connection
    {
        
    protected:
        virtual void send(std::string message) const = 0;
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
        
        // Template method...
        virtual std::string executeRpc(std::string message) const // throw (ConnectionException) 
        {
            send(message);
            return receive();
        }
    };
    
}

#endif // Connection_H_
