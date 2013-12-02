#include "Method.h"

#include <json/json.h>

#include "Connection.h"
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


Method& Method::execute ( const Connection& connection ) // throw (ConnectionException)
{
    std::string responseJson ( connection.executeRpc ( message_.toJson() ) );

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
