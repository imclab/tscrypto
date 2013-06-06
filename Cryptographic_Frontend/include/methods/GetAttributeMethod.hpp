/**
 * @author Francisco Cifuentes [francisco@niclabs.cl]
 */

#ifndef GETATTRIBUTEMETHOD_H
#define GETATTRIBUTEMETHOD_H

#include <Method.hpp>
namespace cf
{

class GetAttributeMethod : public Method
{
protected:
    virtual ResponseMessageStrategy getStrategy() const override;
public:
    GetAttributeMethod(std::string label, std::string id, std::string keyType,
                       int keySize);

};


}

#endif // GETATTRIBUTEMETHOD_H
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
