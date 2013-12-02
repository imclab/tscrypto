#ifndef COMMUNICATION_GENERATERANDOMMETHOD_H
#define COMMUNICATION_GENERATERANDOMMETHOD_H
#include "Method.h"

namespace communication
{

class GenerateRandomMethod : public Method
{
public:
    GenerateRandomMethod ( std::string sessionHandler, long int length );
    virtual ~GenerateRandomMethod() = default;
    virtual ResponseMessage parseResponse ( const Json::Value& value ) override;
};

}

#endif
// kate: indent-mode cstyle; replace-tabs on; 
