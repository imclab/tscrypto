/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "OpenSessionMethod.h"
#include "ResponseMessage.h"
#include <json/json.h>

using namespace communication;

OpenSessionMethod::OpenSessionMethod() : Method ( "OpenSession" )
{
    // NOTE: no tiene argumentos...
}

ResponseMessage* OpenSessionMethod::parseResponse ( const Json::Value& value )
{
    ResponseMessage * response = new ResponseMessage();
    response->addValue ( new StringArgument ( "sessionHandler", value["sessionHandler"].asString() ) );
    return response;
}
