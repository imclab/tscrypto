#include <string>
#include "Argument.h"

using namespace cf;

Argument::Argument(std::string name) 
  : name_(name)
{
}

Argument::~Argument() {}

string Argument::getName() {
  return name_;
}

Argument::operator std::string() const {
  throw BadConversion();
}

Argument::operator int() const {
  throw BadConversion();
}