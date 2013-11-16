/**
 * @file
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef COMMUNICATION_GENERATEKEYPAIRMETHOD_H
#define COMMUNICATION_GENERATEKEYPAIRMETHOD_H

#include "Method.h"

#include <string>
#include <memory>

namespace communication
{

class GenerateKeyPairMethod : public Method
{
public:
    virtual ~GenerateKeyPairMethod() = default;
    GenerateKeyPairMethod ( std::string keyType, int keySize, std::string publicExponent );
    virtual ResponseMessage* parseResponse ( const Json::Value& value ) override;
};

}

#endif // GenerateKeyPairMethod_H_
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
