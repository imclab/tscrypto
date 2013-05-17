#include "Method.h"
#include "Connection.h"

cf::Method::Method(std::string name)
  : message(name)
{
}

void cf::Method::addArgument(cf::Argument* argument)
{
  message.addArgument(argument);
}

std::string cf::Method::execute(const Connection &connection)
{
  std::string response;
  try {
    response = connection.executeRpc(message.toJson());
  } catch(std::exception& e) {
    throw CannotExecuteException(e.what());
  }

  return response;
}
