#ifndef SIGNMETHOD_H
#define SIGNMETHOD_H
#include "Method.hpp"

namespace cf
{

class SignMethod : public Method
{
protected:
  virtual ResponseMessageStrategy getStrategy() const override;
public:
  SignMethod(std::string data);
  virtual ~SignMethod() = default;
};

}

#endif // SIGNMETHOD_H
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
