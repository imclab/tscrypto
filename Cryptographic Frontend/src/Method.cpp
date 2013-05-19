#include "Method.h"
#include "Connection.h"

cf::Method::Method(std::string name)
  : message_(name)
{
}

void cf::Method::addArgument(cf::ArgumentPtr argument)
{
  message_.addArgument(std::move(argument));
}

std::string cf::Method::execute(const Connection &connection)
{
  std::string response;
  try {
    response = connection.executeRpc(message_.toJson());
  } catch(std::exception& e) {
    throw CannotExecuteException(e.what());
  }

  return response;
}
