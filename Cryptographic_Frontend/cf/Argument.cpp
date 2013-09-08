#include <string>
#include "Argument.hpp"

using namespace cf;

Argument::Argument(const std::string & name)
  : name_(name)
{
}

Argument::~Argument() {}

const std::string & Argument::getName() const
{
  return name_;
}

// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
