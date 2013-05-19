#include <string>
#include "Argument.h"

using namespace cf;
using std::string;

Argument::Argument(string name) {
  this->name_ = name;
}

Argument::~Argument() {}

string Argument::getName() {
  return name_;
}

