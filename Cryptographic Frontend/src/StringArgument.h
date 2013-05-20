#ifndef STRINGARGUMENT_H_
#define STRINGARGUMENT_H_

#include <string>
#include "Argument.h"
namespace cf {


  class StringArgument : public Argument {
    private:
      std::string value_;

    public:
      StringArgument(std::string name, std::string value);
      virtual ~StringArgument();
      ArgumentType type() const;
      operator std::string() const;
  };



}

#endif
