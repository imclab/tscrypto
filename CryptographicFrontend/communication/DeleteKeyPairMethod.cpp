#include "DeleteKeyPairMethod.h"
#include "ResponseMessage.h"
#include "Argument.h"
using namespace communication;

DeleteKeyPairMethod::DeleteKeyPairMethod ( std::string handler )
    : Method ( "DeleteKeyPair" )
{
    addArgument ( "handler", handler );
}

ResponseMessage DeleteKeyPairMethod::parseResponse ( const Json::Value& value )
{
    return ResponseMessage();
}
// kate: indent-mode cstyle; replace-tabs on; 
