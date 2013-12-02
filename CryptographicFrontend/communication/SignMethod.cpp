#include "SignMethod.h"

#include "Argument.h"
#include "ResponseMessage.h"

#include <json/json.h>

using namespace communication;

SignMethod::SignMethod ( std::string sessionHandler, std::string data )
    : Method ( "Sign" )
{
    addArgument ( "sessionHandler", sessionHandler );
    addArgument ( "data", data );
}

ResponseMessage SignMethod::parseResponse ( const Json::Value& value )
{
    ResponseMessage response;
    response.addValue ( "signedData", value["signedData"].asString() );

    return response;
}
// kate: indent-mode cstyle; replace-tabs on; 
