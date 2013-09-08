/**
 * @author Francisco Cifuentes [francisco@niclabs.cl]
 */

#include "ResponseMessage.hpp"

using namespace cf;

void ResponseMessage::addValue(ArgumentPtr value)
{
    values_[value->getName()] = std::move(value);
}

// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
