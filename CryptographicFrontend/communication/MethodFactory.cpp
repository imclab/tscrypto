/*
 * <one line to give the library's name and an idea of what it does.>
 * Copyright (C) 2014  Francisco Cifuentes <email>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

#include "MethodFactory.h"
#include <json/json.h>

using namespace communication;

namespace {
ResponseMessage emptyParser(const Json::Value &) {
    return ResponseMessage();
}
}

Method MethodFactory::closeSession(string sessionHandler)
{
    Method method("CloseSession");
    method.addArgument("sessionHandler", sessionHandler);
    method.parser = emptyParser;
    return method;
}

Method MethodFactory::deleteKeyPair(string keyHandler)
{
    Method method("DeleteKeyPair");
    method.addArgument("keyHandler", keyHandler);
    method.parser = emptyParser;

    return method;
}

Method MethodFactory::digestInit(string sessionHandler, string mechanism)
{
    Method method("DigestInit");
    method.addArgument("sessionHandler", sessionHandler);
    method.addArgument("mechanism", mechanism);
    method.parser = emptyParser;

    return method;
}

Method MethodFactory::digest(string sessionHandler, string data)
{
    Method method("Digest");
    method.addArgument("sessionHandler", sessionHandler);
    method.addArgument("data", data);
    method.parser = [](Json::Value const& value) {
        ResponseMessage responseMessage;
        responseMessage.add ( "digest", value["digest"].asString() );
        return responseMessage;
    };

    return method;
}

Method MethodFactory::findKey(string keyHandler)
{
    Method method("FindKey");
    method.addArgument("keyHandler", keyHandler);
    method.parser = [](Json::Value const& value) {
        ResponseMessage response;
        response.add ( "key", value["key"].asString() );
        return response;
    };

    return method;
}

Method MethodFactory::generateKeyPair(string keyType, long int keySize, string publicExponent)
{
    Method method("GenerateKeyPair");
    method.addArgument("keyType", keyType);
    method.addArgument("keySize", keySize);
    method.addArgument("publicExponent", publicExponent);

    method.parser = [](Json::Value const& value) {
        ResponseMessage response;
        response.add ( "keyHandler", value["keyHandler"].asString() );
        response.add ( "modulus", value["modulus"].asString() );
        response.add ( "publicExponent", value["publicExponent"].asString() );
        return response;
    };

    return method;
}

Method MethodFactory::generateRandom(string sessionHandler, long int length)
{
    Method method("GenerateRandom");
    method.addArgument("sessionHandler", sessionHandler);
    method.addArgument("length", length);
    method.parser = [](Json::Value const& value) {
        ResponseMessage response;
        response.add ( "data", value["data"].asString() );
        return response;
    };

    return method;
}

Method MethodFactory::getAttribute(string attribute, string handler)
{
    Method method("GetAttribute");
    method.addArgument("attribute", attribute);
    method.addArgument("handler", handler);
    method.parser = [](Json::Value const& value) {
        ResponseMessage response;
        response.add ( "attributeValue", value["attributeValue"].asString() );
        return response;
    };

    return method;
}

Method MethodFactory::openSession()
{
    Method method("OpenSession");
    method.parser = [](Json::Value const& value) {
        ResponseMessage response;
        response.add ( "sessionHandler", value["sessionHandler"].asString() );
        return response;
    };

    return method;
}

Method MethodFactory::seedRandom(string sessionHandler, string seed)
{
    Method method("SeedRandom");
    method.addArgument("sessionHandler", sessionHandler);
    method.addArgument("seed", seed);
    method.parser = emptyParser;

    return method;
}

Method MethodFactory::signInit(string sessionHandler, string mechanism, string keyHandler)
{
    Method method("SignInit");
    method.addArgument("sessionHandler", sessionHandler);
    method.addArgument("mechanism", mechanism);
    method.addArgument("keyHandler", keyHandler);
    method.parser = emptyParser;

    return method;
}

Method MethodFactory::sign(string sessionHandler, string data)
{
    Method method("Sign");
    method.addArgument("sessionHandler", sessionHandler);
    method.addArgument("data", data);

    method.parser = [](Json::Value const& value) {
        ResponseMessage response;
        response.add ( "signedData", value["signedData"].asString() );
        return response;
    };

    return method;
}

