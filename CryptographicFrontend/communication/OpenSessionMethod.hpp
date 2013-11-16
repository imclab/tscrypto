/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef OPENSESSIONMETHOD_HPP
#define OPENSESSIONMETHOD_HPP

#include "Method.hpp"

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
