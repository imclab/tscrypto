#ifndef SEEDRANDOMMETHOD_HPP
#define SEEDRANDOMMETHOD_HPP
#include "Method.hpp"

namespace cf
{

class SeedRandomMethod : public Method
{
public:
  SeedRandomMethod(std::string data);
  virtual ~SeedRandomMethod() = default;
  virtual ResponseMessagePtr parseResponse(const std::string & message) override;
};

}

#endif // SEEDRANDOMMETHOD_HPP
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
