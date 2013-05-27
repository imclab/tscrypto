#ifndef SIGNMETHOD_H
#define SIGNMETHOD_H
#include <Method.hpp>

namespace cf
{

class SignMethod : public Method
{
protected:
    virtual ResponseMessageStrategy getStrategy() const override;
public:
    SignMethod(std::string data);
};

}

#endif // SIGNMETHOD_H
