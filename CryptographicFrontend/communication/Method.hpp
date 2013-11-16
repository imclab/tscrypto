/**
 * @file
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef COMMUNICATION_METHOD_H
#define COMMUNICATION_METHOD_H

#include <string>
#include <stdexcept>

#include "MethodMessage.hpp"

namespace Json
{
class Value;
}


namespace communication
{

class ResponseMessage;
class Connection;
class Method   // Por ahora los metodos devuelven un string json...
{
private:
    MethodMessage message_;
    std::string response_;
    std::unique_ptr<ResponseMessage> responseMessage_;
protected:

    Method ( const std::string & name );
    
    // This object will manage the lifetime of the argument
    void addArgument ( IArgument* argument );
    
    // Factory Method of the responses.
    virtual ResponseMessage * parseResponse (Json::Value const & value) = 0;

public:
    Method & execute ( Connection const & connection ); // throw (ConnectionException);
    const ResponseMessage & getResponse();
    virtual ~Method() = default;

};

}

#endif // COMMUNICATION_METHOD_H
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
