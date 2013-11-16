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

namespace communication
{

using ArgumentPtr = std::unique_ptr<IArgument>;
class ResponseMessage
{
private:
    std::map<std::string, ArgumentPtr> values_;

public:
    ResponseMessage() = default;
    ResponseMessage ( ResponseMessage & rm ) = delete;
    ResponseMessage & operator= ( ResponseMessage const & rm ) = delete;

    void addValue ( IArgument* value );

    template <typename T> // Hiding the implementation (?)
    T getValue ( const std::string & name ) const {
        return Argument<T>::getValue ( *values_.at ( name ) );
    }

};

}

#endif // RESPONSEMESSAGE_H
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
