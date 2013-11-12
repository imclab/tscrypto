/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "OpenSessionMethod.hpp"
#include "ResponseMessage.hpp"
#include <json/json.h>

using namespace communication;

OpenSessionMethod::OpenSessionMethod() : Method ( "OpenSession" )
{
    // NOTE: no tiene argumentos...
}


ResponseMessagePtr OpenSessionMethod::parseResponse ( const std::string& message )
{
    Json::Value json;
    Json::Reader reader;

    reader.parse ( message, json );

    ResponseMessage * response = new ResponseMessage();
    response->addValue ( new StringArgument ( "sessionHandler", json["sessionHandler"].asString() ) );

    return ResponseMessagePtr ( response );
}
