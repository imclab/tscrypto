#ifndef STRINGARGUMENT_H_
#define STRINGARGUMENT_H_

#include <string>
#include "Argument.h"
namespace cf {


  class StringArgument : public Argument {
    private:
      std::string val;

    public:
      StringArgument(std::string name, std::string value);
      virtual ~StringArgument();
      virtual ArgumentType type() const;
      virtual ArgumentValue value();
  };



}

#endif
