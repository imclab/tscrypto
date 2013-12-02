#include "FindKeyMethod.h"

#include "ResponseMessage.h"
#include "Argument.h"
#include <stdexcept>

#include <json/json.h>

using namespace communication;

FindKeyMethod::FindKeyMethod ( std::string handler )
    : Method ( "FindKey" )
{
    addArgument ( "keyHandler", handler );
}

ResponseMessage FindKeyMethod::parseResponse ( const Json::Value& value )
{
    ResponseMessage response;
    response.addValue ( "key", value["key"].asString() );
    return response;
}
