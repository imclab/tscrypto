#ifndef DELETEKEYPAIRMETHOD_H
#define DELETEKEYPAIRMETHOD_H

#include "Method.hpp"

namespace cf
{

class DeleteKeyPairMethod : public Method
{
public:
    virtual ~DeleteKeyPairMethod() = default;
    DeleteKeyPairMethod(long long handler);
    virtual ResponseMessagePtr parseResponse(const std::string & message) override;
};

}

#endif // DELETEKEYPAIRMETHOD_H
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
