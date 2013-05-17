#ifndef SIGNMETHOD_H
#define SIGNMETHOD_H
#include "Method.h"
#include "MethodMessage.h"

namespace cf{

class SignMethod : public Method
{
public:
  SignMethod(std::string data);
};

}

#endif // SIGNMETHOD_H
