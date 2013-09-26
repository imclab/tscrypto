#include "DeleteKeyPairMethod.hpp"
#include "ResponseMessage.hpp"
#include "Argument.hpp"

cf::DeleteKeyPairMethod::DeleteKeyPairMethod(long long handler)
  : Method("DeleteKeyPair")
{
  addArgument(ArgumentPtr(new IntegerArgument("handler", handler)));
}

cf::ResponseMessagePtr cf::DeleteKeyPairMethod::parseResponse(const std::string & message) {
    return ResponseMessagePtr(new ResponseMessage());
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
