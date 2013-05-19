#include "Method.h"
#include "Connection.h"
#include "GenerateKeyPairMethod.h"

#include "Argument.h"
#include "MethodMessage.h"
#include "StringArgument.h"
#include "IntegerArgument.h"

using namespace cf;
using std::string;

GenerateKeyPairMethod::GenerateKeyPairMethod(string label,
                                             string id,
                                             string keyType,
                                             int keySize)
  : Method("GenerateKeyPair")
{
  /* methodMessage es dueño de los argumentos */
  addArgument(ArgumentPtr(new StringArgument("label", label)));
  addArgument(ArgumentPtr(new StringArgument("id", id)));
  addArgument(ArgumentPtr(new StringArgument("keyType", keyType)));
  addArgument(ArgumentPtr(new IntegerArgument("keySize", keySize)));
}
