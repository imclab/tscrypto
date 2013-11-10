/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "DigestInitMethod.hpp"
#include "Argument.hpp"
#include "ResponseMessage.hpp"

using namespace communication;

DigestInitMethod::DigestInitMethod(std::string sessionHandler, std::string mechanism): Method("DigestInit")
{
  addArgument(new StringArgument("sessionHandler", sessionHandler));
  addArgument(new StringArgument("mechanism", mechanism));
}


ResponseMessagePtr DigestInitMethod::parseResponse(const std::string& message)
{
  return ResponseMessagePtr(new ResponseMessage());
}
