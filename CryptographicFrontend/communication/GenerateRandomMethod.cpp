#include "GenerateRandomMethod.h"

#include "Argument.h"
#include "ResponseMessage.h"

#include <json/json.h>

using namespace communication;

GenerateRandomMethod::GenerateRandomMethod ( std::string sessionHandler, long length )
    : Method ( "GenerateRandom" )
{
    addArgument ( "sessionHandler", sessionHandler );
    addArgument ( "length", length ) ;
}

ResponseMessage GenerateRandomMethod::parseResponse ( const Json::Value& value )
{
    ResponseMessage response;
    response.addValue ( "data", value["data"].asString() );
    return response;
}
