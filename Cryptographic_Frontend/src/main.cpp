#include <Method.hpp>
#include <methods/GenerateKeyPairMethod.hpp>
#include <methods/SignInitMethod.hpp>
#include <methods/SignMethod.hpp>
#include <methods/FindKeyMethod.hpp>
#include <RabbitConnection.hpp>
#include <ResponseMessage.hpp>

#include <iostream>
#include <string>

using namespace cf;

int main()
{
    try {
        RabbitConnection connection("localhost", 5672, "", "rpc_queue", 1);
        std::unique_ptr<Method> method(new GenerateKeyPairMethod("RSA", 1024, "65537"));
        method->execute(connection);
        ResponseMessagePtr response(method->getResponse());

        int handler = response->getValue<int>("handler");
        std::cout << handler << std::endl;

        method.reset(new SignInitMethod("SHA1withRSA", handler));
        method->execute(connection);
        response.reset(method->getResponse().release());
        std::cout << "OK!" << std::endl;

        method.reset(new SignMethod("FFAACCDDBB009966"));
        method->execute(connection);
        response.reset(method->getResponse().release());
        std::cout << response->getValue<std::string>("signedData") << std::endl;

        method.reset(new FindKeyMethod(handler));
        method->execute(connection);
        response.reset(method->getResponse().release());
        std::cout << response->getValue<std::string>("key") << std::endl;

    } catch (std::exception &e) {
        std::cerr << "Error: " << e.what() << std::endl;
    }
    return 0;
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
