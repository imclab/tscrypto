#include "SignInitMethod.hpp"
#include "ResponseMessage.hpp"
#include "Argument.hpp"

cf::SignInitMethod::SignInitMethod(std::string mechanism, std::string privateKeyHandler)
  : Method("SignInit")
{
  addArgument(ArgumentPtr(new StringArgument("mechanism", mechanism)));
  addArgument(ArgumentPtr(new StringArgument("handler", privateKeyHandler)));
}

cf::ResponseMessagePtr cf::SignInitMethod::parseResponse(const std::string & message) {
    return ResponseMessagePtr(new ResponseMessage());
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
