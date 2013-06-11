/**
 * @file
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef GenerateKeyPairMethod_HPP
#define GenerateKeyPairMethod_HPP

#include "Method.hpp"

#include <string>
#include <memory>

namespace cf
{

class GenerateKeyPairMethod : public Method
{

protected:
    virtual ResponseMessageStrategy getStrategy() const override;

public:
    GenerateKeyPairMethod(std::string keyType, int keySize, std::string publicExponent);

};

}

#endif // GenerateKeyPairMethod_H_
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
