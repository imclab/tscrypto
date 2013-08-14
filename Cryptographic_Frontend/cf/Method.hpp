/**
 * @file
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef Method_H_
#define Method_H_

#include <string>
#include <stdexcept>

#include "MethodMessage.hpp"


namespace cf
{

class ResponseMessage;
class Connection;
typedef std::unique_ptr<ResponseMessage> ResponseMessagePtr;
typedef std::function<ResponseMessagePtr(std::string const &)> ResponseMessageStrategy;


class Method   // Por ahora los metodos devuelven un string json...
{
private:
  MethodMessage message_;
  std::string response_;
protected:
  Method(const std::string & name);
  void addArgument(ArgumentPtr argument);
  virtual ResponseMessageStrategy getStrategy() const = 0;

public:
  virtual void execute(Connection const & connection); // throw (ConnectionException);
  virtual ResponseMessagePtr getResponse();
  virtual ~Method() {}

};

}

#endif // Method_H_
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
