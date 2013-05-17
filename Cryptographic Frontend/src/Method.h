/** 
 * @file
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef Method_H_
#define Method_H_

#include <string>
#include <exception>

#include "MethodMessage.h"

namespace cf {

class Connection;

class CannotExecuteException : public std::exception {
  std::string error;
protected:
  CannotExecuteException();
public:
  CannotExecuteException(std::string error_) {
    error = error_;
  }

  const char* what() const throw() {
    return error.c_str();
  }
};

class Method { // Por ahora los metodos devuelven un string json...
private:
  MethodMessage message;
protected:
  Method(std::string name);
  virtual void addArgument(Argument* argument);

public:
  virtual std::string execute(Connection const& connection);
};

} 

#endif // Method_H_
