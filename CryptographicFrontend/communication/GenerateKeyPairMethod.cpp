#include "GenerateKeyPairMethod.h"

#include "Argument.h"
#include "ResponseMessage.h"
#include <json/json.h>

using namespace communication;
using std::string;

GenerateKeyPairMethod::GenerateKeyPairMethod ( string keyType,
        int keySize,
        string publicExponent )
    : Method ( "GenerateKeyPair" )
{
    /* methodMessage es dueño de los argumentos */
    addArgument ( "keyType", keyType );
    addArgument ( "keySize", keySize );
    addArgument ( "publicExponent", publicExponent );
}

ResponseMessage GenerateKeyPairMethod::parseResponse ( const Json::Value& value )
{
    ResponseMessage response;
    response.addValue ( "keyHandler", value["keyHandler"].asString() );
    return response;
}
// kate: indent-mode cstyle; replace-tabs on; 
