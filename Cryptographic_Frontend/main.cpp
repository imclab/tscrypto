#include "cf/Method.hpp"
#include "cf/GenerateKeyPairMethod.hpp"
#include "cf/SignInitMethod.hpp"
#include "cf/SignMethod.hpp"
#include "cf/FindKeyMethod.hpp"
#include "cf/RabbitConnection.hpp"
#include "cf/ResponseMessage.hpp"

#include <iostream>
#include <string>

using namespace cf;

int main()
{
  try {
    long long handler;

    RabbitConnection connection("localhost", 5672, "", "rpc_queue", 1);
    Method * method = new cf::GenerateKeyPairMethod("RSA", 2048, "65537");
    method->execute(connection);
    cf::ResponseMessagePtr response(method->getResponse());
    handler = response->getValue<long long>("handler");
    delete method;

    method = new cf::FindKeyMethod(handler);
    method->execute(connection);
    response.reset(method->getResponse().release());
    std::string pemPublicKey = response->getValue<std::string>("key");
    delete method;

    std::cout << pemPublicKey << std::endl;

  } catch (std::exception & e) {
    std::cerr << "Error: " << e.what() << std::endl;
  }
  return 0;
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
