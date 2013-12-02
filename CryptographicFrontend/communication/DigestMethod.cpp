/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "DigestMethod.h"
#include "Argument.h"
#include "ResponseMessage.h"
#include <json/json.h>

using namespace communication;

DigestMethod::DigestMethod ( std::string sessionHandler, std::string data ) : Method ( "Digest" )
{
    addArgument ( "sessionHandler", sessionHandler );
    addArgument ( "data", data );
}

ResponseMessage DigestMethod::parseResponse ( const Json::Value& value )
{
    ResponseMessage responseMessage;
    responseMessage.addValue ( "digest", value["digest"].asString() );
    return responseMessage;
}
