#include "SeedRandomMethod.hpp"

#include "Argument.hpp"
#include "ResponseMessage.hpp"

#include <json/json.h>

using namespace communication;

SeedRandomMethod::SeedRandomMethod(std::string sessionHandler, std::string seed)
: Method("SeedRandom")
{
  addArgument(new StringArgument("sessionHandler", sessionHandler));
  addArgument(new StringArgument("seed", seed));
}

ResponseMessagePtr SeedRandomMethod::parseResponse(const std::string & message) {
  return ResponseMessagePtr(new ResponseMessage());
}