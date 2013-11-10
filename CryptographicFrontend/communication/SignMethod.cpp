#include "SignMethod.hpp"

#include "Argument.hpp"
#include "ResponseMessage.hpp"

#include <json/json.h>

using namespace communication;

SignMethod::SignMethod(std::string sessionHandler, std::string data)
: Method("Sign")
{
    addArgument(new StringArgument("sessionHandler", sessionHandler));
    addArgument(new StringArgument("data", data));
}

ResponseMessagePtr SignMethod::parseResponse(const std::string & message) {
    Json::Value json;
    Json::Reader reader;
    
    reader.parse(message, json);
    ResponseMessagePtr response(new ResponseMessage());
    response->addValue(new StringArgument("signedData", json["signedData"].asString()));
    
    return response;
}



// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
