/*
Copyright 2013 NIC Chile Research Labs.
This file is part of PKCS11-TsCrypto.

PKCS11-TsCrypto is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

PKCS11-TsCrypto is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with PKCS11-TsCrypto.  If not, see <http://www.gnu.org/licenses/>.
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
