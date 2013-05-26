#include <methods/SignMethod.hpp>

#include <StringArgument.hpp>
#include <ResponseMessage.hpp>

#include <json/json.h>

using namespace cf;

SignMethod::SignMethod(std::string data)
    : Method("Sign")
{
    addArgument(ArgumentPtr(new StringArgument("data", data)));
}

ResponseMessageStrategy SignMethod::getStrategy() const
{
      return [](std::string const& message) -> ResponseMessagePtr { 
      Json::Value json;
      Json::Reader reader;
      
      reader.parse(message, json);
      ResponseMessagePtr response(new ResponseMessage());
      response->setValue("signedData", json["signedData"].asString());
      
      return std::move(response);
  };
}



