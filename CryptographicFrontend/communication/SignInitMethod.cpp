#include "SignInitMethod.hpp"
#include "ResponseMessage.hpp"
#include "Argument.hpp"

using namespace communication;

SignInitMethod::SignInitMethod ( std::string sessionHandler, std::string mechanism, std::string privateKeyHandler )
    : Method ( "SignInit" )
{
    addArgument ( new StringArgument ( "sessionHandler", sessionHandler ) );
    addArgument ( new StringArgument ( "mechanism", mechanism ) );
    addArgument ( new StringArgument ( "keyHandler", privateKeyHandler ) );
}

ResponseMessagePtr SignInitMethod::parseResponse ( const std::string & message )
{
    return ResponseMessagePtr ( new ResponseMessage() );
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
