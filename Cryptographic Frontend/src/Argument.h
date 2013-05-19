#ifndef ARGUMENT_H_
#define ARGUMENT_H_

#include <string>
#include <boost/variant.hpp>

namespace cf {

enum class ArgumentType { String, Integer };
struct ArgumentValue {
private:
  boost::variant<int, std::string> value; // El maravilloso variant!
public:
  ArgumentValue(std::string s) : value(s){}
  ArgumentValue(int i) : value(i){}
  explicit operator std::string() { return boost::get<std::string>(value); }
  explicit operator int() {return boost::get<int>(value);}
};

class Argument {
  protected:
    std::string name_;

  public:
    Argument(std::string name_);
    virtual ~Argument();
    virtual std::string getName();
    virtual ArgumentType type() const = 0;
    virtual ArgumentValue value() = 0;
};

}

#endif
