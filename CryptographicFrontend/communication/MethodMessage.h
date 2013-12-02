#ifndef COMMUNICATION_METHODMESSAGE_H
#define COMMUNICATION_METHODMESSAGE_H

#include <string>
#include "Argument.h"

namespace communication
{

class MethodMessage
{
private:
    std::string name_;
    argument::Map argMap_;

public:
    MethodMessage ( const std::string & name );
    MethodMessage (MethodMessage && message) = default;
    MethodMessage (const MethodMessage & message) = default;

    virtual ~MethodMessage() = default;
    virtual void addArgument(argument::Name name, argument::Value value);
    virtual std::string toJson();
};


}

#endif
// kate: indent-mode cstyle; replace-tabs on; 
