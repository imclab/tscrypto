#include "GenerateKeyPairMethod.hpp"

#include "Argument.hpp"
#include "ResponseMessage.hpp"
#include <json/json.h>

using namespace communication;
using std::string;

GenerateKeyPairMethod::GenerateKeyPairMethod ( string keyType,
        int keySize,
        string publicExponent )
    : Method ( "GenerateKeyPair" )
{
    /* methodMessage es dueño de los argumentos */
    addArgument ( new StringArgument ( "keyType", keyType ) );
    addArgument ( new IntegerArgument ( "keySize", keySize ) );
    addArgument ( new StringArgument ( "publicExponent", publicExponent ) );
}

ResponseMessage* GenerateKeyPairMethod::parseResponse ( const Json::Value& value )
{
    ResponseMessage * response = new ResponseMessage();
    response->addValue ( new StringArgument ( "keyHandler", value["keyHandler"].asString() ) );
    return response;
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
