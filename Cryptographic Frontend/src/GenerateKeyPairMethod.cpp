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
  Argument* labelArg = new StringArgument("label", label);
  Argument* idArg = new StringArgument("id", id);
  Argument* keyTypeArg = new StringArgument("keyType", keyType);
  Argument* keySizeArg = new IntegerArgument("keySize", keySize);

  /* methodMessage es dueño de los argumentos */
  addArgument(labelArg);
  addArgument(idArg);
  addArgument(keyTypeArg);
  addArgument(keySizeArg);
}
