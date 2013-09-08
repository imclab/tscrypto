#include "GenerateKeyPairMethod.hpp"

#include "Argument.hpp"
#include "ResponseMessage.hpp"
#include <json/json.h>

using namespace cf;
using std::string;

GenerateKeyPairMethod::GenerateKeyPairMethod(string keyType, 
                                             int keySize, 
                                             string publicExponent)
  : Method("GenerateKeyPair")
{
  /* methodMessage es dueño de los argumentos */
  addArgument(ArgumentPtr(new StringArgument("keyType", keyType)));
  addArgument(ArgumentPtr(new IntegerArgument("keySize", keySize)));
  addArgument(ArgumentPtr(new StringArgument("publicExponent", publicExponent)));
}

ResponseMessageStrategy GenerateKeyPairMethod::getStrategy() const
{
  return [](std::string const & message) -> ResponseMessagePtr {
    Json::Value json;
    Json::Reader reader;

    reader.parse(message, json);
    ResponseMessagePtr response(new ResponseMessage());

    response->addValue(ArgumentPtr( new IntegerArgument("handler", json["handler"].asInt64()) ));

    return std::move(response);
  };
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
