#include <Method.hpp>
#include <Connection.hpp>
#include <ResponseMessage.hpp>

cf::Method::Method(std::string name)
    : message_(name)
{
}

void cf::Method::addArgument(cf::ArgumentPtr argument)
{
    message_.addArgument(std::move(argument));
}

void cf::Method::execute(Connection const &connection) throw (ConnectionException)
{
    response_ = connection.executeRpc(message_.toJson());
}

cf::ResponseMessagePtr cf::Method::getResponse()
{
    return cf::ResponseMessage::responseMessageFactory(response_, getStrategy());
}
