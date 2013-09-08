#ifndef SIGNINITMETHOD_H
#define SIGNINITMETHOD_H

#include "Method.hpp"

namespace cf
{

class SignInitMethod : public Method
{
public:
    virtual ~SignInitMethod() = default;
    SignInitMethod(std::string mechanism, long long privateKeyHandler);
    virtual ResponseMessagePtr parseResponse(const std::string & message) override;
};

}

#endif // SIGNINITMETHOD_H
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
