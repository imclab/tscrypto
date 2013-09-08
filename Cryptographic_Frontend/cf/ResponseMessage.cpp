/**
 * @author Francisco Cifuentes [francisco@niclabs.cl]
 */

#include <json/json.h>
#include <stdexcept>

#include "ResponseMessage.hpp"

using namespace cf;

ResponseMessagePtr ResponseMessage::responseMessageFactory(const std::string & message,
    ResponseMessageStrategy createResponseMessage)
{
  Json::Value json;
  Json::Reader reader;

  if (!reader.parse(message, json)) {
    throw std::invalid_argument("El mensaje de respuesta fue incapaz de ser parseado");
  }

  std::string rc = json["returnCode"].asString();

  if (rc != "OK") {
    throw std::runtime_error(rc);
  }

  Json::Value const & value = json["value"];

  return createResponseMessage(value.toStyledString());
}

ResponseMessage::ResponseMessage()
{
}

void ResponseMessage::setValue(std::string name, ArgumentPtr value)
{
  values_[name] = std::move(value);
}


// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
