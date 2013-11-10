/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef CF_CLOSESESSIONMETHOD_HPP
#define CF_CLOSESESSIONMETHOD_HPP

#include "Method.hpp"

namespace communication {
  
  class CloseSessionMethod : public communication::Method
  {
  public:
    CloseSessionMethod(std::string sessionHandler);
    virtual ~CloseSessionMethod();    
  protected:
    virtual ResponseMessagePtr parseResponse(const std::string& message);
  };
}

#endif // CF_CLOSESESSIONMETHOD_H
