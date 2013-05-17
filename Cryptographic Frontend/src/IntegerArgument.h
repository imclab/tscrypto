#ifndef INTEGERARGUMENT_H_
#define INTEGERARGUMENT_H_

#include <string>

#include "Argument.h"

namespace cf {
class IntegerArgument : public Argument {
  private:
    int val;
  public:
    IntegerArgument(std::string name, int value);
    virtual ~IntegerArgument();
    virtual ArgumentType type() const;
    virtual ArgumentValue value();
};
}

#endif
