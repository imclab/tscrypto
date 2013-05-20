#ifndef ARGUMENT_H_
#define ARGUMENT_H_

#include <string>
#include <boost/variant.hpp>
#include <exception>

namespace cf {

enum class ArgumentType { String, Integer };
class Argument {
  class BadConversion : public std::exception {
    char const* what() const throw() {
	return "Error al realizar conversion de datos";
    }
  };
  protected:
    std::string name_;

  public:
    Argument(std::string name_);
    virtual ~Argument();
    virtual std::string getName();
    virtual ArgumentType type() const = 0;
    virtual explicit operator std::string() const;
    virtual explicit operator int() const;
};

}

#endif
