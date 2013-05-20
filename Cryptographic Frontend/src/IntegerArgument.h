#ifndef INTEGERARGUMENT_H_
#define INTEGERARGUMENT_H_

#include <string>

#include "Argument.h"

namespace cf {
class IntegerArgument : public Argument {
  private:
    int value_;
  public:
    IntegerArgument(std::string name, int value);
    virtual ~IntegerArgument();
    ArgumentType type() const;
    operator int() const;
};
}

#endif
