#include "FindKeyMethod.hpp"

#include "ResponseMessage.hpp"
#include "Argument.hpp"
#include <stdexcept>

#include <json/json.h>

cf::FindKeyMethod::FindKeyMethod(long long handler)
  : Method("FindKey")
{
  addArgument(ArgumentPtr(new IntegerArgument("handler", handler)));
}

cf::ResponseMessageStrategy cf::FindKeyMethod::getStrategy() const
{
  return [](std::string const & message) -> ResponseMessagePtr {
    Json::Value json;
    Json::Reader reader;

    reader.parse(message, json);
    ResponseMessagePtr response(new ResponseMessage());

    response->addValue(ArgumentPtr( new StringArgument("key", json["key"].asString()) ));

    return std::move(response);
  };
}
