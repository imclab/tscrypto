/**
 * @author Francisco Cifuentes [francisco@niclabs.cl]
 */

#include <ResponseMessage.hpp>
#include <json/json.h>
#include <stdexcept>

using namespace cf;

ResponseMessagePtr ResponseMessage::responseMessageFactory(const std::string &message,
        ResponseMessageStrategy createResponseMessage)
{
    Json::Value json;
    Json::Reader reader;

    if (!reader.parse(message, json)) {
        throw std::invalid_argument("El mensaje de respuesta fue incapaz de ser parseado");
    }

    std::string rc = json["returnCode"].asString();

    if (rc != "OK") {
        // TODO: return BadResponse... Pensar en algun metodo de esto (Excepciones?)
    }

    Json::Value const &value = json["value"];

    return createResponseMessage(value.toStyledString());
}

ResponseMessage::ResponseMessage()
    : values()
{
}

ResponseMessage &ResponseMessage::operator=(const ResponseMessage &rm)
{
    if (this == &rm)
        return *this;

    values = rm.values;
    return *this;
}

ResponseMessage::ResponseMessage(ResponseMessage &rm)
    : values(rm.values)
{
}

void ResponseMessage::setValue(std::string name, boost::any value)
{
    values[name] = value;
}


// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
