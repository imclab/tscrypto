#include <string>
#include "StringArgument.hpp"

using namespace cf;

StringArgument::StringArgument(const std::string &name, const std::string &value)
    : Argument(name), value_(value)
{
}

StringArgument::~StringArgument() {}

void StringArgument::accept(ArgumentVisitor &visitor)
{
    visitor.visit(*this);
}

std::string StringArgument::value()
{
    return value_;
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
