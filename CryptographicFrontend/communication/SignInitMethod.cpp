#include "SignInitMethod.h"
#include "ResponseMessage.h"
#include "Argument.h"

using namespace communication;

SignInitMethod::SignInitMethod ( std::string sessionHandler, std::string mechanism, std::string privateKeyHandler )
    : Method ( "SignInit" )
{
    addArgument ( new StringArgument ( "sessionHandler", sessionHandler ) );
    addArgument ( new StringArgument ( "mechanism", mechanism ) );
    addArgument ( new StringArgument ( "keyHandler", privateKeyHandler ) );
}

ResponseMessage* SignInitMethod::parseResponse ( const Json::Value& value )
{
    return new ResponseMessage;
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
