#include "DeleteKeyPairMethod.hpp"
#include "ResponseMessage.hpp"
#include "Argument.hpp"
using namespace communication;

DeleteKeyPairMethod::DeleteKeyPairMethod(std::string handler)
  : Method("DeleteKeyPair")
{
  addArgument(new StringArgument("handler", handler));
}

ResponseMessagePtr DeleteKeyPairMethod::parseResponse(const std::string & message) {
    return ResponseMessagePtr(new ResponseMessage());
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
