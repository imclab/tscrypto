#ifndef COMMUNICATION_FINDKEYMETHOD_H
#define COMMUNICATION_FINDKEYMETHOD_H
#include "Method.h"
namespace communication
{

class FindKeyMethod : public Method
{
public:
    virtual ~FindKeyMethod() = default;
    virtual ResponseMessage* parseResponse ( const Json::Value& value ) override;
    FindKeyMethod ( std::string handler );
};

}

#endif // FINDKEYMETHOD_H
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
