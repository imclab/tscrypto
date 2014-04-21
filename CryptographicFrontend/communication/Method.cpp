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

#include "Method.h"

#include <json/json.h>

#include "RPC.h"
#include "ResponseMessage.h"

using namespace communication;

Method::Method ( const std::string & name )
    : message_ ( name )
{
}

void Method::addArgument ( argument::Name name, argument::Value value )
{
    message_.addArgument(name, value);
}


Method& Method::execute ( AbstractRPC & connection ) // throw (ConnectionException)
{
    std::string responseJson ( connection.execute ( message_.toJson() ) );

    Json::Value json;
    Json::Reader reader;

    if ( !reader.parse ( responseJson, json ) ) {
        throw std::invalid_argument ( "El mensaje de respuesta fue incapaz de ser parseado" );
    }

    std::string rc = json["returnCode"].asString();

    if ( rc != "OK" ) {
        throw std::runtime_error ( rc );
    }

    Json::Value const & value = json["value"];

    responseMessage_ = parseResponse(value);

    return *this;
}

const ResponseMessage & Method::getResponse()
{
    return responseMessage_;
}

ResponseMessage Method::parseResponse(const Json::Value& value)
{
    if(parser) {
	return parser(value);
    } else {
	return ResponseMessage();
    }
}