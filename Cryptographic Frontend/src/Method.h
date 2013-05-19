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

class Method { // Por ahora los metodos devuelven un string json...
private:
  MethodMessage message_;
protected:
  Method(std::string name);
  virtual void addArgument(ArgumentPtr argument);

public:
  class CannotExecuteException : public std::exception {
    std::string error_;
  protected:
    CannotExecuteException();
  public:
    CannotExecuteException(std::string error) {
      error_ = error;
    }

    const char* what() const throw() {
      return error_.c_str();
    }
  };
  virtual std::string execute(Connection const& connection);
};

} 

#endif // Method_H_
