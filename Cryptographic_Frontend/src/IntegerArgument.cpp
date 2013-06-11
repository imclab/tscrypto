#include <string>
#include "IntegerArgument.hpp"

using namespace cf;
using std::string;

IntegerArgument::IntegerArgument(const string &name, int value)
    : Argument(name), value_(value)
{

}

IntegerArgument::~IntegerArgument() {}

void IntegerArgument::accept(ArgumentVisitor &visitor)
{
    visitor.visit(*this);
}

int IntegerArgument::value()
{
    return value_;
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
