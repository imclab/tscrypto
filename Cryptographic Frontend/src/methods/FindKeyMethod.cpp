#include <methods/FindKeyMethod.hpp>

#include <ResponseMessage.hpp>
#include <StringArgument.hpp>
#include <stdexcept>

#include <json/json.h>

cf::FindKeyMethod::FindKeyMethod(std::string keyType, std::string id)
: Method("FindKey")
{
    if (keyType == "privateKey")
        privateKey_ = true;
    else if (keyType == "publicKey")
        privateKey_ = false;
    else
        throw std::runtime_error("Tipo de llave desconocido");
    
    addArgument(ArgumentPtr(new StringArgument("keyType", keyType)));
    addArgument(ArgumentPtr(new StringArgument("id", id)));
}

cf::ResponseMessageStrategy cf::FindKeyMethod::getStrategy() const
{ // Una verdadera clausura, captura todo el objeto =)...
    return [this](std::string const& message) -> ResponseMessagePtr {
        Json::Value json;
        Json::Reader reader;
        
        reader.parse(message, json);
        ResponseMessagePtr response(new ResponseMessage());
        
        if (privateKey_)
            response->setValue("handler", json["handler"].asInt());
        else
            response->setValue("key", json["key"].asString());
        
        return std::move(response);
    };
}
