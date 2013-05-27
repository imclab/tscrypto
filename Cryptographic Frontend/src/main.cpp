#include <Method.hpp>
#include <methods/GenerateKeyPairMethod.hpp>
#include <methods/SignInitMethod.hpp>
#include <methods/SignMethod.hpp>
#include <RabbitConnection.hpp>
#include <ResponseMessage.hpp>

#include <iostream>
#include <string>

using namespace cf;

int main()
{
    try {
        RabbitConnection connection("localhost", 5672, "", "rpc_queue", 1);
        std::unique_ptr<Method> method(new GenerateKeyPairMethod("hola", "mundo", "RSA", 1024));
        method->execute(connection);
        ResponseMessagePtr response(method->getResponse());
        
        // Esta interfaz probablemente cambiará a algo mejor, pero por mientras sirve...
        std::cout << response->getValue<int>("publicKeyHandler") << std::endl; 
        
        method.reset(new SignInitMethod("SHA1withRSA", response->getValue<int>("privateKeyHandler")));
        method->execute(connection);
        response.reset(method->getResponse().release());
        std::cout << "OK!" << std::endl;
        
        method.reset(new SignMethod("FFAACCDDBB009966"));
        method->execute(connection);
        response.reset(method->getResponse().release());
        std::cout << response->getValue<std::string>("signedData") << std::endl;
        
        
    } catch (std::exception &e) {
        std::cerr << "Error: " << e.what() << std::endl;
    }
    return 0;
}
