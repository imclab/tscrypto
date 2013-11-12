#ifndef SIGNMETHOD_H
#define SIGNMETHOD_H
#include "Method.hpp"

namespace communication
{

class SignMethod : public Method
{
public:
    SignMethod ( std::string sessionHandler, std::string data );
    virtual ~SignMethod() = default;
    virtual ResponseMessagePtr parseResponse ( const std::string & message ) override;
};

}

#endif // SIGNMETHOD_H
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
