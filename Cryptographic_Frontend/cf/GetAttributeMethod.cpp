/**
 * @author Francisco Cifuentes [francisco@niclabs.cl]
 */

#include "GetAttributeMethod.hpp"
#include "Argument.hpp"
#include "ResponseMessage.hpp"

#include <json/json.h>

cf::GetAttributeMethod::GetAttributeMethod(std::string attribute, std::string handler)
  : Method("GetAttribute")
{
  addArgument(ArgumentPtr(new StringArgument("attribute", attribute)));
  addArgument(ArgumentPtr(new StringArgument("handler", handler)));
}

cf::ResponseMessagePtr cf::GetAttributeMethod::parseResponse(const std::string & message) {
    Json::Value json;
    Json::Reader reader;
    
    reader.parse(message, json);
    ResponseMessagePtr response(new ResponseMessage());

    response->addValue(ArgumentPtr( new StringArgument("attributeValue", json["attributeValue"].asString()) ));

    return response;
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
