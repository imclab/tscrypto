#include <string>
#include "StringArgument.h"

using namespace cf;

StringArgument::StringArgument(std::string name, std::string value) : Argument(name) {
  val = value;
}

StringArgument::~StringArgument() {}

ArgumentType StringArgument::type() const {
  return ArgumentType::String;
}

ArgumentValue StringArgument::value() {
  return ArgumentValue {.s = val } ;
}

