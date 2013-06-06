#ifndef METHODMESSAGE_H_
#define METHODMESSAGE_H_

#include <vector>
#include <string>
#include <memory>

// #include <Argument.hpp>
namespace cf
{

// Ojo un ArgumentPtr es un unique_ptr, por lo
// tanto hay que usar std::move para ser explicito
// en quien es el dueño del puntero.
class Argument;
typedef std::unique_ptr<Argument> ArgumentPtr;

class MethodMessage
{
private:
    std::string name_;
    std::vector<ArgumentPtr> argList_;

public:
    MethodMessage(const std::string &name);
    virtual ~MethodMessage();
    virtual void addArgument(ArgumentPtr arg);
    virtual std::string toJson();
};


}

#endif
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
