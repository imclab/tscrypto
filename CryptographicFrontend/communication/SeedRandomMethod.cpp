#include "SeedRandomMethod.h"

#include "Argument.h"
#include "ResponseMessage.h"

#include <json/json.h>

using namespace communication;

SeedRandomMethod::SeedRandomMethod ( std::string sessionHandler, std::string seed )
    : Method ( "SeedRandom" )
{
    addArgument ( new StringArgument ( "sessionHandler", sessionHandler ) );
    addArgument ( new StringArgument ( "seed", seed ) );
}

ResponseMessage* SeedRandomMethod::parseResponse ( const Json::Value& value )
{
    return new ResponseMessage;
}