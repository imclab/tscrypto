#ifndef SIGNINITMETHOD_H
#define SIGNINITMETHOD_H

#include "Method.hpp"

namespace cf
{

class SignInitMethod : public Method
{
protected:
    virtual ResponseMessageStrategy getStrategy() const override;
public:
    virtual ~SignInitMethod() = default;
    SignInitMethod(std::string mechanism, long long privateKeyHandler);
};

}

#endif // SIGNINITMETHOD_H
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
