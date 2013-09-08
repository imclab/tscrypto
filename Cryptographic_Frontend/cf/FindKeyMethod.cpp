#include "FindKeyMethod.hpp"

#include "ResponseMessage.hpp"
#include "Argument.hpp"
#include <stdexcept>

#include <json/json.h>

using namespace cf;

FindKeyMethod::FindKeyMethod(long long handler)
  : Method("FindKey")
{
  addArgument(ArgumentPtr(new IntegerArgument("handler", handler)));
}

ResponseMessagePtr FindKeyMethod::parseResponse(const std::string & message)
{
  Json::Value json;
  Json::Reader reader;
  
  reader.parse(message, json);
  
  ResponseMessagePtr response(new ResponseMessage());
  response->addValue(ArgumentPtr( new StringArgument("key", json["key"].asString()) ));
  
  return std::move(response);
}