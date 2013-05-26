#ifndef SIGNINITMETHOD_H
#define SIGNINITMETHOD_H

#include <Method.hpp>

namespace cf
{

class SignInitMethod : public Method
{
public:
    SignInitMethod(std::string mechanism, int privateKeyHandler);
    virtual ResponseMessageStrategy getStrategy() const;
};

}

#endif // SIGNINITMETHOD_H
