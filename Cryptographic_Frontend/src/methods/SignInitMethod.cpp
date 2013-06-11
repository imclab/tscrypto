#include "methods/SignInitMethod.hpp"
#include "ResponseMessage.hpp"

#include "StringArgument.hpp"
#include "IntegerArgument.hpp"

cf::SignInitMethod::SignInitMethod(std::string mechanism, int privateKeyHandler)
    : Method("SignInit")
{
    addArgument(ArgumentPtr(new StringArgument("mechanism", mechanism)));
    addArgument(ArgumentPtr(new IntegerArgument("handler", privateKeyHandler)));
}


cf::ResponseMessageStrategy cf::SignInitMethod::getStrategy() const
{
    return [](std::string const & message) -> ResponseMessagePtr {
        ResponseMessagePtr response(new ResponseMessage());
        return std::move(response);
    };
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
