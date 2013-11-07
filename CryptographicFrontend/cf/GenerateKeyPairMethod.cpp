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

ResponseMessagePtr GenerateKeyPairMethod::parseResponse(const std::string & message) {
    Json::Value json;
    Json::Reader reader;
    
    reader.parse(message, json);

    ResponseMessagePtr response(new ResponseMessage());
    response->addValue(ArgumentPtr( new StringArgument("handler", json["handler"].asString()) ));

    return response;
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
