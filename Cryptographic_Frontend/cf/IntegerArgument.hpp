#include "Argument.hpp"
#if 0
namespace cf
{
class IntegerArgument : public Argument
{
private:
  long long value_;
public:
  IntegerArgument(const std::string & name, long long value);
  virtual ~IntegerArgument();
  virtual void accept(ArgumentVisitor & visitor);
  virtual long long value();
};
}

#endif
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
