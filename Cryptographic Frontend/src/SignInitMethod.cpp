#include "SignInitMethod.h"
#include "Connection.h"

#include "StringArgument.h"
#include "IntegerArgument.h"

cf::SignInitMethod::SignInitMethod(std::string mechanism, int privateKeyHandler)
  : Method("SignInit")
{
  addArgument(ArgumentPtr(new StringArgument("mechanism", mechanism)));
  addArgument(ArgumentPtr(new IntegerArgument("privateKeyHandler",
                                  privateKeyHandler)));
}
