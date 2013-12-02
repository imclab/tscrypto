#include "SeedRandomMethod.h"

#include "Argument.h"
#include "ResponseMessage.h"

#include <json/json.h>

using namespace communication;

SeedRandomMethod::SeedRandomMethod ( std::string sessionHandler, std::string seed )
    : Method ( "SeedRandom" )
{
    addArgument ( "sessionHandler", sessionHandler );
    addArgument ( "seed", seed );
}

ResponseMessage SeedRandomMethod::parseResponse ( const Json::Value& value )
{
    return ResponseMessage();
}
