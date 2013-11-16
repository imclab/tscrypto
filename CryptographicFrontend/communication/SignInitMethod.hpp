#ifndef SIGNINITMETHOD_H
#define SIGNINITMETHOD_H

#include "Method.hpp"

namespace communication
{

class SignInitMethod : public Method
{
public:
    virtual ~SignInitMethod() = default;
    SignInitMethod ( std::string sessionHandler, std::string mechanism, std::string privateKeyHandler );
    virtual ResponseMessage* parseResponse ( const Json::Value& value ) override;
};

}

#endif // SIGNINITMETHOD_H
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
