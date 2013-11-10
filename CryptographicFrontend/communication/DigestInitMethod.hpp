/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef CF_DIGESTINIT_H
#define CF_DIGESTINIT_H

#include "Method.hpp"

namespace communication {

class DigestInitMethod : public Method
{
public:
    DigestInitMethod(std::string sessionHandler, std::string mechanism);
    virtual ~DigestInitMethod() = default;
protected:
    virtual communication::ResponseMessagePtr parseResponse(const std::string& message);
};
}

#endif // CF_DIGESTINIT_H
