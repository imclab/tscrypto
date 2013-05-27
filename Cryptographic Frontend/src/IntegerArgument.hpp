#ifndef INTEGERARGUMENT_H_
#define INTEGERARGUMENT_H_

#include <string>

#include <Argument.hpp>

namespace cf
{
class IntegerArgument : public Argument
{
private:
    int value_;
public:
    IntegerArgument(std::string name, int value);
    virtual ~IntegerArgument();
    virtual void accept(ArgumentVisitor &visitor);
    virtual int value();
};
}

#endif
