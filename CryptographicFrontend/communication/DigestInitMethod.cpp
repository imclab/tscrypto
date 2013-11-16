/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "DigestInitMethod.h"
#include "Argument.h"
#include "ResponseMessage.h"

using namespace communication;

DigestInitMethod::DigestInitMethod ( std::string sessionHandler, std::string mechanism ) : Method ( "DigestInit" )
{
    addArgument ( new StringArgument ( "sessionHandler", sessionHandler ) );
    addArgument ( new StringArgument ( "mechanism", mechanism ) );
}

ResponseMessage* DigestInitMethod::parseResponse ( const Json::Value& value )
{
    return new ResponseMessage();
}
