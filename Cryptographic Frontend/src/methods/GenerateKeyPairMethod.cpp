#include <methods/GenerateKeyPairMethod.hpp>

#include <StringArgument.hpp>
#include <IntegerArgument.hpp>
#include <ResponseMessage.hpp>
#include <json/json.h>

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

ResponseMessageStrategy GenerateKeyPairMethod::getStrategy() const
{
    return [](std::string const& message) -> ResponseMessagePtr { 
        Json::Value json;
        Json::Reader reader;
        
        reader.parse(message, json);
        ResponseMessagePtr response(new ResponseMessage());
        
        response->setValue("publicKeyHandler", json["publicKeyHandler"].asInt());
        response->setValue("privateKeyHandler", json["privateKeyHandler"].asInt());
        
        return std::move(response);
    };
}