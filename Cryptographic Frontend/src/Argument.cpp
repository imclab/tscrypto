#include <string>
#include <Argument.hpp>

using namespace cf;

Argument::Argument(std::string name)
    : name_(name)
{
}

Argument::~Argument() {}

std::string Argument::getName()
{
    return name_;
}

Argument::operator std::string() const
{
    throw BadConversion();
}

Argument::operator int() const
{
    throw BadConversion();
}
