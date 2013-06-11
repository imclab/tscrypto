/**
 * @author Francisco Cifuentes [francisco@niclabs.cl]
 */

#ifndef GETATTRIBUTEMETHOD_H
#define GETATTRIBUTEMETHOD_H

#include "Method.hpp"
namespace cf
{

class GetAttributeMethod : public Method
{
protected:
    virtual ResponseMessageStrategy getStrategy() const override;
public:
    GetAttributeMethod(std::string attribute, int handler);

};


}

#endif // GETATTRIBUTEMETHOD_H
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
