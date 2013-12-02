/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef COMMUNICATION_CLOSESESSIONMETHOD_H
#define COMMUNICATION_CLOSESESSIONMETHOD_H

#include "Method.h"

namespace communication
{

class CloseSessionMethod : public communication::Method
{
public:
    CloseSessionMethod ( std::string sessionHandler );
    virtual ResponseMessage parseResponse ( const Json::Value& value ) override;
    virtual ~CloseSessionMethod() = default;
};
}

#endif // CF_CLOSESESSIONMETHOD_H
