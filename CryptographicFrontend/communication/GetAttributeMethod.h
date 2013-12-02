/**
 * @author Francisco Cifuentes [francisco@niclabs.cl]
 */

#ifndef COMMUNICATION_GETATTRIBUTEMETHOD_H
#define COMMUNICATION_GETATTRIBUTEMETHOD_H

#include "Method.h"
namespace communication
{

class GetAttributeMethod : public Method
{
public:
    virtual ~GetAttributeMethod() = default;
    GetAttributeMethod ( std::string attribute, std::string handler );
    virtual ResponseMessage parseResponse ( const Json::Value& value ) override;

};


}

#endif // GETATTRIBUTEMETHOD_H
// kate: indent-mode cstyle; replace-tabs on; 
