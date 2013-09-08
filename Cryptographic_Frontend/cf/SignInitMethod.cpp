#include "SignInitMethod.hpp"
#include "ResponseMessage.hpp"
#include "Argument.hpp"

cf::SignInitMethod::SignInitMethod(std::string mechanism, long long privateKeyHandler)
  : Method("SignInit")
{
  addArgument(ArgumentPtr(new StringArgument("mechanism", mechanism)));
  addArgument(ArgumentPtr(new IntegerArgument("handler", privateKeyHandler)));
}

cf::ResponseMessagePtr cf::SignInitMethod::parseResponse(const std::string & message) {
    ResponseMessagePtr response(new ResponseMessage());
    return std::move(response);
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
