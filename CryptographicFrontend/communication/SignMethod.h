#ifndef COMMUNICATION_SIGNMETHOD_H
#define COMMUNICATION_SIGNMETHOD_H
#include "Method.h"

namespace communication
{

class SignMethod : public Method
{
public:
    SignMethod ( std::string sessionHandler, std::string data );
    virtual ~SignMethod() = default;
    virtual ResponseMessage* parseResponse ( const Json::Value& value ) override;
};

}

#endif // SIGNMETHOD_H
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
