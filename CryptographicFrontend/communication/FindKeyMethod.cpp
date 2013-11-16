#include "FindKeyMethod.hpp"

#include "ResponseMessage.hpp"
#include "Argument.hpp"
#include <stdexcept>

#include <json/json.h>

using namespace communication;

FindKeyMethod::FindKeyMethod ( std::string handler )
    : Method ( "FindKey" )
{
    addArgument ( new StringArgument ( "keyHandler", handler ) );
}

ResponseMessage* FindKeyMethod::parseResponse ( const Json::Value& value )
{
    ResponseMessage * response = new ResponseMessage;
    response->addValue ( new StringArgument ( "key", value["key"].asString() ) );
    return response;
}