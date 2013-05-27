#include <string>
#include <StringArgument.hpp>

using namespace cf;

StringArgument::StringArgument(std::string name, std::string value)
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
