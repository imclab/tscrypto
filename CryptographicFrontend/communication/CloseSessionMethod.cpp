/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "CloseSessionMethod.h"
#include "ResponseMessage.h"

using namespace communication;

CloseSessionMethod::CloseSessionMethod ( std::string sessionHandler ) : Method ( "CloseSession" )
{
    addArgument ( new StringArgument ( "sessionHandler", sessionHandler ) );
}

ResponseMessage* CloseSessionMethod::parseResponse ( const Json::Value& value )
{
    return new ResponseMessage;
}