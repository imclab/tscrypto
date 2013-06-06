#ifndef ARGUMENT_H_
#define ARGUMENT_H_

#include <string>
#include <exception>

namespace cf
{

class Argument;
class ArgumentVisitor;

// Que tipos de argumentos existen?
class StringArgument;
class IntegerArgument;

class ArgumentVisitor
{
public:
    virtual void visit(Argument &arg) = 0;
    virtual void visit(StringArgument &arg) = 0;
    virtual void visit(IntegerArgument &arg) = 0;
};

class Argument
{
protected:
    std::string name_;

public:
    Argument(const std::string &name);
    virtual ~Argument();
    virtual std::string getName();
    virtual void accept(ArgumentVisitor &visitor) = 0;

};

}

#endif
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
