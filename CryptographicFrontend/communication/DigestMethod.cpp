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

ResponseMessagePtr DigestMethod::parseResponse ( const std::string& message )
{
    Json::Value json;
    Json::Reader reader;

    reader.parse ( message, json );

    ResponseMessage * responseMessage = new ResponseMessage();
    responseMessage->addValue ( new StringArgument ( "digest", json["digest"].asString() ) );

    return ResponseMessagePtr ( responseMessage );
}
