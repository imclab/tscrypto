#ifndef COMMUNICATION_ARGUMENT_H
#define COMMUNICATION_ARGUMENT_H

#include <string>
#include <stdexcept>
#include <typeinfo>

namespace communication
{

class IArgument
{
public:
    virtual const std::string& getName() const = 0;
    virtual const std::type_info & getTypeId() const = 0;
};

template <typename T>
class Argument : public IArgument
{
private:
    const std::string name_;
    const T value_;
    static constexpr const std::type_info & type = typeid ( T );
public:
    static bool match ( const IArgument & argument ) {
        return argument.getTypeId() == type;
    }

    static const T & getValue ( const IArgument & a ) {
        if ( match ( a ) ) {
            return static_cast<const Argument<T> &> ( a ).getValue();
        }

        throw std::logic_error ( "Type mismatch!" );
    }

    Argument ( const std::string & name, const T & value )
        : name_ ( name ), value_ ( value ) {

    }

    virtual ~Argument() = default;

    virtual const std::string & getName() const override {
        return name_;
    }

    virtual const T & getValue() const {
        return value_;
    }

    virtual const std::type_info & getTypeId() const override {
        return type;
    }
};

// Instancias validas...
//typedef Argument<std::string> StringArgument;
//typedef Argument<long long> IntegerArgument;
using StringArgument = Argument<std::string>;
using IntegerArgument = Argument<long long>;

}

#endif
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
