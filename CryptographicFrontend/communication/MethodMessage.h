#ifndef COMMUNICATION_METHODMESSAGE_H
#define COMMUNICATION_METHODMESSAGE_H

#include <vector>
#include <string>
#include <memory>

namespace communication
{

class IArgument;

class MethodMessage
{
private:
    std::string name_;
    
    using ArgumentPtr = std::unique_ptr<IArgument>;
    std::vector<ArgumentPtr> argList_;

public:
    MethodMessage ( const std::string & name );
    virtual ~MethodMessage();
    virtual void addArgument ( IArgument * arg );
    virtual std::string toJson();
};


}

#endif
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
