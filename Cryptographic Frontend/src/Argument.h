#ifndef ARGUMENT_H_
#define ARGUMENT_H_

#include <string>

namespace cf {

enum class ArgumentType { String, Integer };
struct ArgumentValue { 
  std::string s; 
  union {
    int i;
  };
};

class Argument {
  protected:
    std::string name;

  public:
    Argument(std::string name);
    virtual ~Argument();
    virtual std::string getName();
    virtual ArgumentType type() const = 0;
    virtual ArgumentValue value() = 0;
};

}

#endif
