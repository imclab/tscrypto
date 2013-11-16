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
    addArgument ( new StringArgument ( "attribute", attribute ) );
    addArgument ( new StringArgument ( "handler", handler ) );
}

ResponseMessage* GetAttributeMethod::parseResponse ( const Json::Value& value )
{
    ResponseMessage * response = new ResponseMessage;
    response->addValue ( new StringArgument ( "attributeValue", value["attributeValue"].asString() ) );
    return response;
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
