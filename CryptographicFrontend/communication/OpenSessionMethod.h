/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef COMMUNICATION_OPENSESSIONMETHOD_HPP
#define COMMUNICATION_OPENSESSIONMETHOD_HPP

#include "Method.h"

namespace communication
{
class OpenSessionMethod : public Method
{
public:
    OpenSessionMethod();
    virtual ~OpenSessionMethod() = default;
    virtual ResponseMessage* parseResponse ( const Json::Value& value ) override;
};
}

#endif // OPENSESSIONMETHOD_H
