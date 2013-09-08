#include <json/json.h>

#include "Method.hpp"
#include "Connection.hpp"
#include "ResponseMessage.hpp"
#include "Argument.hpp"

cf::Method::Method(const std::string & name)
: message_(name)
{
}

void cf::Method::addArgument(cf::ArgumentPtr argument)
{
    message_.addArgument(std::move(argument));
}

void cf::Method::execute(Connection const & connection) // throw (ConnectionException)
{
    response_ = connection.executeRpc(message_.toJson());
}

const cf::ResponseMessage & cf::Method::getResponse() {
    if (responseMessage_ == nullptr) {
        Json::Value json;
        Json::Reader reader;
        
        if (!reader.parse(response_, json)) {
            throw std::invalid_argument("El mensaje de respuesta fue incapaz de ser parseado");
        }
        
        std::string rc = json["returnCode"].asString();
        
        if (rc != "OK") {
            throw std::runtime_error(rc);
        }
        
        Json::Value const & value = json["value"];
        Json::FastWriter writer;
        responseMessage_ = parseResponse(writer.write(value));
    }
    
    return *responseMessage_;
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
