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
    SignInitMethod(std::string mechanism, int privateKeyHandler);
};

}

#endif // SIGNINITMETHOD_H
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
