/**
 * @author Francisco Cifuentes [francisco@niclabs.cl]
 */

#include <methods/GetAttributeMethod.hpp>
#include <StringArgument.hpp>
#include <IntegerArgument.hpp>
#include <ResponseMessage.hpp>

#include <json/json.h>

cf::GetAttributeMethod::GetAttributeMethod(std::string label,
        std::string id,
        std::string keyType,
        int keySize)
    : Method("GetAttribute")
{
    addArgument(ArgumentPtr(new StringArgument("label", label)));
    addArgument(ArgumentPtr(new StringArgument("id", id)));
    addArgument(ArgumentPtr(new StringArgument("keyType", keyType)));
    addArgument(ArgumentPtr(new IntegerArgument("keySize", keySize)));
}

cf::ResponseMessageStrategy cf::GetAttributeMethod::getStrategy() const
{
    return [](std::string const & message) -> ResponseMessagePtr {
        Json::Value json;
        Json::Reader reader;

        reader.parse(message, json);
        ResponseMessagePtr response(new ResponseMessage());

        response->setValue("attributeValue", json["attributeValue"].asString());

        return std::move(response);
    };
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
