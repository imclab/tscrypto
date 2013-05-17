/** 
 * @file 
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */ 

#ifndef Connection_H_
#define Connection_H_

#include <string>
#include <exception>
#include <amqp.h>
#include <amqp_framing.h>

namespace cf {

  class BadResponseException : public std::exception {
    const char* what() const throw() {
      return "Error al recibir respuesta";
    }
  };

  class Connection { 
    protected:
      virtual void send(std::string message) const = 0;
      virtual std::string receive() const = 0;
    public: 
      // Trait...
      virtual std::string executeRpc(std::string message) const {
        send(message);
        return receive();
      }
  }; 

} 

#endif // Connection_H_
