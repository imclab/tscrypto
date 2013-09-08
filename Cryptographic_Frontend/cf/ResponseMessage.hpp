/**
 * @author Francisco Cifuentes [francisco@niclabs.cl]
 */

#ifndef RESPONSEMESSAGE_H
#define RESPONSEMESSAGE_H

#include <memory>
#include <string>
#include <map>
#include <functional>

#include "Argument.hpp"

namespace cf
{

class ResponseMessage;
using ResponseMessagePtr = std::unique_ptr<ResponseMessage>;
using ResponseMessageStrategy = std::function<ResponseMessagePtr(std::string const &)>;
using ArgumentPtr = std::unique_ptr<IArgument>;

class ResponseMessage
{
private:
  std::map<std::string, ArgumentPtr> values_;

public:
  static ResponseMessagePtr responseMessageFactory(const std::string & message,
      ResponseMessageStrategy strategy);

  ResponseMessage();
  ResponseMessage(ResponseMessage & rm) = delete;
  ResponseMessage & operator=(ResponseMessage const & rm) = delete;

  void setValue(std::string name, ArgumentPtr value);
  
  template <typename T> // Escondo la implementacion...
  T getValue(const std::string & name) {
    const ArgumentPtr & value = values_.at(name);
    return Argument<T>::getValue(*value);
  }
};

}

#endif // RESPONSEMESSAGE_H
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
