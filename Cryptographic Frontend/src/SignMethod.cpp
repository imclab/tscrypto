#include "SignMethod.h"
#include "StringArgument.h"

cf::SignMethod::SignMethod(std::string data)
  : Method("Sign")
{
  addArgument(ArgumentPtr(new StringArgument("data", data)));
}

