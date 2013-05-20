#include <string>
#include "IntegerArgument.h"

using namespace cf;
using std::string;

IntegerArgument::IntegerArgument(string name, int value) : Argument(name) {
  value_ = value;
}

IntegerArgument::~IntegerArgument() {}

ArgumentType IntegerArgument::type() const {
  return ArgumentType::Integer;
}

IntegerArgument::operator int() const {
  return value_;
}