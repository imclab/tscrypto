#include "GenerateRandomMethod.h"

#include "Argument.h"
#include "ResponseMessage.h"

#include <json/json.h>

using namespace communication;

GenerateRandomMethod::GenerateRandomMethod ( std::string sessionHandler, long length )
    : Method ( "GenerateRandom" )
{
    addArgument ( new StringArgument ( "sessionHandler", sessionHandler ) );
    addArgument ( new IntegerArgument ( "length", length ) );
}

ResponseMessage* GenerateRandomMethod::parseResponse ( const Json::Value& value )
{
    ResponseMessage * response = new ResponseMessage();
    response->addValue ( new StringArgument ( "data", value["data"].asString() ) );
    return response;
}