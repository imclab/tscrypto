/**
 * @author Francisco Cifuentes [francisco@niclabs.cl]
 */

#include "ResponseMessage.h"
#include "Argument.h"

using namespace communication;

/*
ResponseMessage::ResponseMessage(ResponseMessage&& other)
: values_(std::move(other.values_))
{
}

ResponseMessage& ResponseMessage::operator=(ResponseMessage&& rhs)
{
  values_ = std::move(rhs.values_);
  return *this;
}
*/

void ResponseMessage::addValue ( argument::Name name, argument::Value value )
{
    values_[name] = value;
}


// kate: indent-mode cstyle; replace-tabs on; 
