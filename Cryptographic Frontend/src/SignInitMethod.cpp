#include "SignInitMethod.h"
#include "Connection.h"

#include "StringArgument.h"
#include "IntegerArgument.h"

cf::SignInitMethod::SignInitMethod(std::string mechanism, int privateKeyHandler)
  : Method("SignInit")
{
  addArgument(new StringArgument("mechanism", mechanism));
  addArgument(new IntegerArgument("privateKeyHandler",
                                  privateKeyHandler));
}
