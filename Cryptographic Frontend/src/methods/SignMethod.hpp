#ifndef SIGNMETHOD_H
#define SIGNMETHOD_H
#include <Method.hpp>

namespace cf
{

class SignMethod : public Method
{
public:
  SignMethod(std::string data);
  virtual ResponseMessageStrategy getStrategy() const;
};

}

#endif // SIGNMETHOD_H
