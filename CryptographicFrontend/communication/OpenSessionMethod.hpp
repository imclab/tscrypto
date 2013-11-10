/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef OPENSESSIONMETHOD_HPP
#define OPENSESSIONMETHOD_HPP

#include "Method.hpp"

namespace communication { 
  class OpenSessionMethod : public Method
  {
  public:
    OpenSessionMethod();
    virtual ~OpenSessionMethod() = default;
    virtual communication::ResponseMessagePtr parseResponse(const std::string& message);
  };
}

#endif // OPENSESSIONMETHOD_H
