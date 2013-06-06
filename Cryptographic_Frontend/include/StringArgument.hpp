#ifndef STRINGARGUMENT_H_
#define STRINGARGUMENT_H_

#include <string>
#include <Argument.hpp>
namespace cf
{


class StringArgument : public Argument
{
private:
    std::string value_;

public:
    StringArgument(const std::string &name, const std::string &value);
    virtual ~StringArgument();
    virtual void accept(ArgumentVisitor &visitor);
    virtual std::string value();
};



}

#endif
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
