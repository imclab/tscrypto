#ifndef FINDKEYMETHOD_HPP
#define FINDKEYMETHOD_HPP
#include "Method.hpp"
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
