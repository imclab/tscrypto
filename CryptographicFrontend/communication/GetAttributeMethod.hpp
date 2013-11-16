/**
 * @author Francisco Cifuentes [francisco@niclabs.cl]
 */

#ifndef GETATTRIBUTEMETHOD_H
#define GETATTRIBUTEMETHOD_H

#include "Method.hpp"
namespace communication
{

class GetAttributeMethod : public Method
{
public:
    virtual ~GetAttributeMethod() = default;
    GetAttributeMethod ( std::string attribute, std::string handler );
    virtual ResponseMessage* parseResponse ( const Json::Value& value ) override;

};


}

#endif // GETATTRIBUTEMETHOD_H
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
