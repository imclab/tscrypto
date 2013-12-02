/**
 * @file
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef COMMUNICATION_METHOD_H
#define COMMUNICATION_METHOD_H

#include <string>
#include <stdexcept>

#include "MethodMessage.h"
#include "ResponseMessage.h"

namespace Json
{
class Value;
}


namespace communication
{

class Connection;
class Method
{
private:
    MethodMessage message_;
    ResponseMessage responseMessage_;
protected:
    Method ( const std::string & name );

    virtual void addArgument(argument::Name name, argument::Value value);

    // Factory Method of the responses.
    virtual ResponseMessage parseResponse (Json::Value const & value) = 0;

public:
    Method & execute ( Connection const & connection ); // throw (ConnectionException);
    const ResponseMessage & getResponse();
    virtual ~Method() = default;

};

}

#endif // COMMUNICATION_METHOD_H
// kate: indent-mode cstyle; replace-tabs on; 

