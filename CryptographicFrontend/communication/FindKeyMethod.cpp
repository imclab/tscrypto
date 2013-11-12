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

ResponseMessagePtr FindKeyMethod::parseResponse ( const std::string & message )
{
    Json::Value json;
    Json::Reader reader;

    reader.parse ( message, json );

    ResponseMessagePtr response ( new ResponseMessage() );
    response->addValue ( new StringArgument ( "key", json["key"].asString() ) );

    return response;
}
