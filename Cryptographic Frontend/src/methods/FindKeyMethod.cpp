#include <methods/FindKeyMethod.hpp>

#include <ResponseMessage.hpp>
#include <StringArgument.hpp>

#include <json/json.h>

cf::FindKeyMethod::FindKeyMethod(std::string keyType, std::string id)
: Method("FindKey")
{
  addArgument(ArgumentPtr(new StringArgument("keyType", keyType)));
  addArgument(ArgumentPtr(new StringArgument("id", id)));
}

cf::ResponseMessageStrategy cf::FindKeyMethod::getStrategy() const
{
    return [](std::string const& message) -> ResponseMessagePtr { 
        Json::Value json;
        Json::Reader reader;
        
        reader.parse(message, json);
        ResponseMessagePtr response(new ResponseMessage());
        
        response->setValue("handler", json["handler"].asInt());
        return std::move(response);
    };
}
