#include "Method.h"
#include "GenerateKeyPairMethod.h"
#include "SignInitMethod.h"
#include "SignMethod.h"
#include "RabbitConnection.h"

#include <iostream>

using namespace cf;
int main()
{
  RabbitConnection connection("localhost", 5672, "", "rpc_queue", 1);
  Method* method = new GenerateKeyPairMethod("hola", "mundo", "RSA", 1024);
  std::string response = method->execute(connection);
  std::cout << response << std::endl;
  return 0;
}
