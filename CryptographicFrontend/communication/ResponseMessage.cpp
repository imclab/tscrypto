/**
 * @author Francisco Cifuentes [francisco@niclabs.cl]
 */

#include "ResponseMessage.hpp"
#include "Argument.hpp"


using namespace communication;

void ResponseMessage::addValue ( IArgument * value )
{
    values_[value->getName()].reset ( value );
}

// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
