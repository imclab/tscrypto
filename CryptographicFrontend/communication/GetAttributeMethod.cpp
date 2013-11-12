/**
 * @author Francisco Cifuentes [francisco@niclabs.cl]
 */

#include "GetAttributeMethod.hpp"
#include "Argument.hpp"
#include "ResponseMessage.hpp"

#include <json/json.h>

using namespace communication;

GetAttributeMethod::GetAttributeMethod ( std::string attribute, std::string handler )
    : Method ( "GetAttribute" )
{
    addArgument ( new StringArgument ( "attribute", attribute ) );
    addArgument ( new StringArgument ( "handler", handler ) );
}

ResponseMessagePtr GetAttributeMethod::parseResponse ( const std::string & message )
{
    Json::Value json;
    Json::Reader reader;

    reader.parse ( message, json );
    ResponseMessagePtr response ( new ResponseMessage() );

    response->addValue ( new StringArgument ( "attributeValue", json["attributeValue"].asString() ) );

    return response;
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
