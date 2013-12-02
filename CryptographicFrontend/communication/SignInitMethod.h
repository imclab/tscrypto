#ifndef COMMUNICATION_SIGNINITMETHOD_H
#define COMMUNICATION_SIGNINITMETHOD_H

#include "Method.h"

namespace communication
{

class SignInitMethod : public Method
{
public:
    virtual ~SignInitMethod() = default;
    SignInitMethod ( std::string sessionHandler, std::string mechanism, std::string privateKeyHandler );
    virtual ResponseMessage parseResponse ( const Json::Value& value ) override;
};

}

#endif // SIGNINITMETHOD_H
// kate: indent-mode cstyle; replace-tabs on; 
