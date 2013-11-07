#include "SeedRandomMethod.hpp"

#include "Argument.hpp"
#include "ResponseMessage.hpp"

#include <json/json.h>

using namespace cf;

SeedRandomMethod::SeedRandomMethod(std::string seed)
: Method("SeedRandom")
{
  addArgument(ArgumentPtr(new StringArgument("seed", seed)));
}

ResponseMessagePtr SeedRandomMethod::parseResponse(const std::string & message) {
  return ResponseMessagePtr(new ResponseMessage());
}