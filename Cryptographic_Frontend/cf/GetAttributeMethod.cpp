/**
 * @author Francisco Cifuentes [francisco@niclabs.cl]
 */

#include "GetAttributeMethod.hpp"
#include "Argument.hpp"
#include "ResponseMessage.hpp"

#include <json/json.h>

cf::GetAttributeMethod::GetAttributeMethod(std::string attribute, int handler)
  : Method("GetAttribute")
{
  addArgument(ArgumentPtr(new StringArgument("attribute", attribute)));
  addArgument(ArgumentPtr(new IntegerArgument("handler", handler)));
}

cf::ResponseMessageStrategy cf::GetAttributeMethod::getStrategy() const
{
  return [](std::string const & message) -> ResponseMessagePtr {
    Json::Value json;
    Json::Reader reader;

    reader.parse(message, json);
    ResponseMessagePtr response(new ResponseMessage());

    response->setValue("attributeValue", ArgumentPtr(new StringArgument("attributeValue", json["attributeValue"].asString())) );

    return std::move(response);
  };
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
