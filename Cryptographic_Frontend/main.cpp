#include "cf/Method.hpp"
#include "cf/FindKeyMethod.hpp"
#include "cf/GenerateKeyPairMethod.hpp"
#include "cf/GetAttributeMethod.hpp"
#include "cf/SignInitMethod.hpp"
#include "cf/SignMethod.hpp"
#include "cf/RabbitConnection.hpp"
#include "cf/ResponseMessage.hpp"

#include <iostream>
#include <string>

using namespace cf;

class DumpConnection : public Connection {
    std::ostream & out;
public:
    DumpConnection(std::ostream& stream) : out(stream) {}
    
    DumpConnection() = delete;
    virtual ~DumpConnection() = default;
    
    virtual void send(const std::string & msg) const override {
        out << msg << std::endl;
    }
    
    virtual std::string receive() const override {
        return std::string();
    }
};

int main()
{
    
    long long handler = 123456789;
    DumpConnection connection(std::cout);
    
    cf::FindKeyMethod fk(handler);
    fk.execute(connection);
    
    cf::GenerateKeyPairMethod gkp("RSA", 1024, "65537");
    gkp.execute(connection);   
    
    cf::GetAttributeMethod ga("id", handler);
    ga.execute(connection);
    
    cf::SignInitMethod si("RSA", handler);
    si.execute(connection);
    
    cf::SignMethod s("Datos a firmar.");
    s.execute(connection);
    
    
    return 0;
}
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
