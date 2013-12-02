/**
 * @author Francisco Cifuentes [francisco@niclabs.cl]
 */

#ifndef COMMUNICATION_RESPONSEMESSAGE_H
#define COMMUNICATION_RESPONSEMESSAGE_H

#include <memory>
#include <string>
#include <unordered_map>

#include "Argument.h"

namespace communication
{

class ResponseMessage
{
private:
    argument::Map values_;

public:
    ResponseMessage() = default;
    ResponseMessage ( ResponseMessage const & other ) = default;
    ResponseMessage ( ResponseMessage && other ) = default;
    ResponseMessage & operator= ( ResponseMessage const & rhs ) = default;
    ResponseMessage & operator= ( ResponseMessage && rhs ) = default;

    void addValue ( argument::Name name, argument::Value value );

    template <typename T> // Hiding the implementation (?)
    T getValue ( const argument::Name & name ) const {
        return argument::get<T>( values_.at ( name ) );
    }

};

}

#endif // RESPONSEMESSAGE_H
// kate: indent-mode cstyle; replace-tabs on; 
