#include <string>
#include <StringArgument.hpp>

using namespace cf;

StringArgument::StringArgument(std::string name, std::string value)
    : Argument(name), value_(value)
{
}

StringArgument::~StringArgument() {}

ArgumentType StringArgument::type() const
{
    return ArgumentType::String;
}

StringArgument::operator std::string() const
{
    return value_;
}
