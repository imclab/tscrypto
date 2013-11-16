#ifndef DELETEKEYPAIRMETHOD_H
#define DELETEKEYPAIRMETHOD_H

#include "Method.hpp"
#include <string>

namespace communication
{

class DeleteKeyPairMethod : public Method
{
public:
    virtual ~DeleteKeyPairMethod() = default;
    DeleteKeyPairMethod ( std::string handler );    
    virtual ResponseMessage* parseResponse ( const Json::Value& value ) override;    
};

}

#endif // DELETEKEYPAIRMETHOD_H
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
