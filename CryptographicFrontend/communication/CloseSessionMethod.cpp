/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "CloseSessionMethod.hpp"
#include "ResponseMessage.hpp"

using namespace communication;

CloseSessionMethod::CloseSessionMethod ( std::string sessionHandler ) : Method ( "CloseSession" )
{
    addArgument ( new StringArgument ( "sessionHandler", sessionHandler ) );
}

ResponseMessage* CloseSessionMethod::parseResponse ( const Json::Value& value )
{
    return new ResponseMessage;
}