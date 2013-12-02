/**
 * @author Francisco Cifuentes [francisco@niclabs.cl]
 */

#include "GetAttributeMethod.h"
#include "Argument.h"
#include "ResponseMessage.h"

#include <json/json.h>

using namespace communication;

GetAttributeMethod::GetAttributeMethod ( std::string attribute, std::string handler )
    : Method ( "GetAttribute" )
{
    addArgument ( "attribute", attribute );
    addArgument ( "handler", handler );
}

ResponseMessage GetAttributeMethod::parseResponse ( const Json::Value& value )
{
    ResponseMessage response;
    response.addValue ( "attributeValue", value["attributeValue"].asString() );
    return response;
}
// kate: indent-mode cstyle; replace-tabs on; 
