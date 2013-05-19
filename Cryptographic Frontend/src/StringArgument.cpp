#include <string>
#include "StringArgument.h"

using namespace cf;

StringArgument::StringArgument(std::string name, std::string value)
  : Argument(name), value_(value)
{
}

StringArgument::~StringArgument() {}

ArgumentType StringArgument::type() const {
  return ArgumentType::String;
}

ArgumentValue StringArgument::value() {
  ArgumentValue value(value_);
  return value;
}

