#include <string>
#include "IntegerArgument.hpp"

using namespace cf;
using std::string;

IntegerArgument::IntegerArgument(const string & name, long long value)
  : Argument(name), value_(value)
{

}

IntegerArgument::~IntegerArgument() {}

void IntegerArgument::accept(ArgumentVisitor & visitor)
{
  visitor.visit(*this);
}

long long IntegerArgument::value()
{
  return value_;
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
