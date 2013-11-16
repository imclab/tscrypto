#include "SignMethod.hpp"

#include "Argument.hpp"
#include "ResponseMessage.hpp"

#include <json/json.h>

using namespace communication;

SignMethod::SignMethod ( std::string sessionHandler, std::string data )
    : Method ( "Sign" )
{
    addArgument ( new StringArgument ( "sessionHandler", sessionHandler ) );
    addArgument ( new StringArgument ( "data", data ) );
}

ResponseMessage* SignMethod::parseResponse ( const Json::Value& value )
{
    ResponseMessage * response = new ResponseMessage;
    response->addValue ( new StringArgument ( "signedData", value["signedData"].asString() ) );

    return response;
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
