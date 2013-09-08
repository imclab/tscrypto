#include "Argument.hpp"
#if 0
namespace cf
{


class StringArgument : public Argument
{
private:
  const std::string value_;

public:
  StringArgument(const std::string & name, const std::string & value);
  virtual ~StringArgument();
  virtual void accept(ArgumentVisitor & visitor);
  virtual const std::string & value() const;
};



}

#endif
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
