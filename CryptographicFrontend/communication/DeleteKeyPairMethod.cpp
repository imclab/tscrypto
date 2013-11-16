#include "DeleteKeyPairMethod.h"
#include "ResponseMessage.h"
#include "Argument.h"
using namespace communication;

DeleteKeyPairMethod::DeleteKeyPairMethod ( std::string handler )
    : Method ( "DeleteKeyPair" )
{
    addArgument ( new StringArgument ( "handler", handler ) );
}

ResponseMessage* DeleteKeyPairMethod::parseResponse ( const Json::Value& value )
{
    return new ResponseMessage;
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
