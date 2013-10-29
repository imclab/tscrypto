#include "GenerateRandomMethod.hpp"

#include "Argument.hpp"
#include "ResponseMessage.hpp"

#include <json/json.h>

using namespace cf;

GenerateRandomMethod::GenerateRandomMethod(long length)
: Method("GenerateRandom")
{
  addArgument(ArgumentPtr(new IntegerArgument("length", length)));
}

ResponseMessagePtr GenerateRandomMethod::parseResponse(const std::string & message) {
  Json::Value json;
  Json::Reader reader;  
  reader.parse(message, json);
  
  ResponseMessagePtr response(new ResponseMessage());
  response->addValue( ArgumentPtr( new StringArgument("data", json["data"].asString())) );
    
  return response;
}