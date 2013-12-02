/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef COMMUNICATION_DIGESTINIT_H
#define COMMUNICATION_DIGESTINIT_H

#include "Method.h"

namespace communication
{

class DigestInitMethod : public Method
{
public:
    DigestInitMethod ( std::string sessionHandler, std::string mechanism );
    virtual ResponseMessage parseResponse ( const Json::Value& value ) override;
    virtual ~DigestInitMethod() = default;
};
}

#endif // CF_DIGESTINIT_H
