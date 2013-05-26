/**
 * @author Francisco Cifuentes [francisco@niclabs.cl]
 */

#ifndef GETATTRIBUTEMETHOD_H
#define GETATTRIBUTEMETHOD_H

#include <Method.hpp>
namespace cf {

class GetAttributeMethod : public Method
{
public:
    GetAttributeMethod(std::string label, std::string id, std::string keyType, 
		       int keySize);

    virtual ResponseMessageStrategy getStrategy() const;
};


}

#endif // GETATTRIBUTEMETHOD_H
