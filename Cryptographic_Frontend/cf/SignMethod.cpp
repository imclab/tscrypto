#include "SignMethod.hpp"

#include "Argument.hpp"
#include "ResponseMessage.hpp"

#include <json/json.h>

using namespace cf;

SignMethod::SignMethod(std::string data)
: Method("Sign")
{
    addArgument(ArgumentPtr(new StringArgument("data", data)));
}

ResponseMessagePtr SignMethod::parseResponse(const std::string & message) {
    Json::Value json;
    Json::Reader reader;
    
    reader.parse(message, json);
    ResponseMessagePtr response(new ResponseMessage());
    response->addValue( ArgumentPtr( new StringArgument("signedData", json["signedData"].asString())) );
    
    return std::move(response);
}



// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
