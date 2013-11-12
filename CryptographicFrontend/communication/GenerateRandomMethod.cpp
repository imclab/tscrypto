#include "GenerateRandomMethod.hpp"

#include "Argument.hpp"
#include "ResponseMessage.hpp"

#include <json/json.h>

using namespace communication;

GenerateRandomMethod::GenerateRandomMethod ( std::string sessionHandler, long length )
    : Method ( "GenerateRandom" )
{
    addArgument ( new StringArgument ( "sessionHandler", sessionHandler ) );
    addArgument ( new IntegerArgument ( "length", length ) );
}

ResponseMessagePtr GenerateRandomMethod::parseResponse ( const std::string & message )
{
    Json::Value json;
    Json::Reader reader;
    reader.parse ( message, json );

    ResponseMessagePtr response ( new ResponseMessage() );
    response->addValue ( new StringArgument ( "data", json["data"].asString() ) );

    return response;
}
