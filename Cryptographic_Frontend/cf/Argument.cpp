#include "Argument.hpp"

bool cf::is(const IArgument & argument, const std::type_info & type) {
  return argument.getTypeId() == type;
}

// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
