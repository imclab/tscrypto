#ifndef METHODMESSAGE_H_
#define METHODMESSAGE_H_

#include <vector>
#include <string>
#include "Argument.h"
namespace cf {

  class MethodMessage {
    private:
      std::string name;
      std::vector<Argument*> argList;

    public:
      MethodMessage(std::string name);
      virtual ~MethodMessage();
      /* MethodMessage se adueña del argumento,
       * es el responsable de su memoria.*/
      virtual void addArgument(Argument* arg);
      virtual std::string toJson();
  };


}

#endif
