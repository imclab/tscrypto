#ifndef SIGNINITMETHOD_H
#define SIGNINITMETHOD_H
#include "Method.h"
#include "MethodMessage.h"

namespace cf {

class SignInitMethod : public Method
{
public:
  SignInitMethod(std::string mechanism, int privateKeyHandler);
};

}

#endif // SIGNINITMETHOD_H
