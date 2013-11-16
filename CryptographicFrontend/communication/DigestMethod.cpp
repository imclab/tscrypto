/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "DigestMethod.hpp"
#include "Argument.hpp"
#include "ResponseMessage.hpp"
#include <json/json.h>

using namespace communication;

DigestMethod::DigestMethod ( std::string sessionHandler, std::string data ) : Method ( "Digest" )
{
    addArgument ( new StringArgument ( "sessionHandler", sessionHandler ) );
    addArgument ( new StringArgument ( "data", data ) );
}

ResponseMessage* DigestMethod::parseResponse ( const Json::Value& value )
{
    ResponseMessage * responseMessage = new ResponseMessage();
    responseMessage->addValue ( new StringArgument ( "digest", value["digest"].asString() ) );
    return responseMessage;
}