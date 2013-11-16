/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef COMMUNICATION_DIGESTMETHOD_H
#define COMMUNICATION_DIGESTMETHOD_H

#include "Method.h"

namespace communication
{

class DigestMethod : public communication::Method
{
public:
    DigestMethod ( std::string sessionHandler, std::string data );
    virtual ~DigestMethod() = default;
protected:
    virtual ResponseMessage* parseResponse ( const Json::Value& value ) override;
};
}

#endif // CF_DIGESTMETHOD_H
