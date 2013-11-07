#include "DeleteKeyPairMethod.hpp"
#include "ResponseMessage.hpp"
#include "Argument.hpp"

cf::DeleteKeyPairMethod::DeleteKeyPairMethod(std::string handler)
  : Method("DeleteKeyPair")
{
  addArgument(ArgumentPtr(new StringArgument("handler", handler)));
}

cf::ResponseMessagePtr cf::DeleteKeyPairMethod::parseResponse(const std::string & message) {
    return ResponseMessagePtr(new ResponseMessage());
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
