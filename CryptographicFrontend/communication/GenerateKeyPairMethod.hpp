/**
 * @file
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef GenerateKeyPairMethod_HPP
#define GenerateKeyPairMethod_HPP

#include "Method.hpp"

#include <string>
#include <memory>

namespace communication
{
    
    class GenerateKeyPairMethod : public Method
    {
    public:
        virtual ~GenerateKeyPairMethod() = default;
        GenerateKeyPairMethod(std::string keyType, int keySize, std::string publicExponent);
        virtual ResponseMessagePtr parseResponse(const std::string & message) override;
        
    };
    
}

#endif // GenerateKeyPairMethod_H_
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
