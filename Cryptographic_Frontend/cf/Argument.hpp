#ifndef ARGUMENT_H_
#define ARGUMENT_H_

#include <string>
#include <stdexcept>
#include <typeinfo>

namespace cf
{

class IArgument {
public:
    virtual const std::string& getName() const = 0;
    virtual const std::type_info & getTypeId() const = 0;    
};

bool is(const IArgument & argument, const std::type_info & type);

template <typename T>
class Argument : public IArgument
{
private:
    const std::string name_;
    const T value_;
public:
    static constexpr const std::type_info & type = typeid(T);
    
    static const T & getValue(const IArgument & a) {
        if (is(a, type))
            return static_cast<const Argument<T> &>(a).getValue();
        
        throw std::logic_error("Type mismatch!");
    }
    
    Argument(const std::string & name, const T & value) 
    : name_(name), value_(value)
    {
        
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

using StringArgument = Argument<std::string>;
using IntegerArgument = Argument<long long>;

}

#endif
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
