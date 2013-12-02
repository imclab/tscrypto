#include "SignInitMethod.h"
#include "ResponseMessage.h"
#include "Argument.h"

using namespace communication;

SignInitMethod::SignInitMethod ( std::string sessionHandler, std::string mechanism, std::string privateKeyHandler )
    : Method ( "SignInit" )
{
    addArgument ( "sessionHandler", sessionHandler );
    addArgument ( "mechanism", mechanism );
    addArgument ( "keyHandler", privateKeyHandler );
}

ResponseMessage SignInitMethod::parseResponse ( const Json::Value& value )
{
    return ResponseMessage();
}
// kate: indent-mode cstyle; replace-tabs on; 
