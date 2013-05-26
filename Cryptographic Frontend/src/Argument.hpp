#ifndef ARGUMENT_H_
#define ARGUMENT_H_

#include <string>
#include <boost/variant.hpp>
#include <exception>

namespace cf
{

/* Los tipos de argumentos posibles */
enum class ArgumentType
{
    String, Integer
};
class Argument
{
protected:
    std::string name_;

public:
    class BadConversion : public std::exception
    {
        char const *what() const throw() {
            return "Error al realizar conversion de datos";
        }
    };
    Argument(std::string name_);
    virtual ~Argument();
    virtual std::string getName();
    virtual ArgumentType type() const = 0;
    /* A ser implementado por el hijo,  por ahora solo
     * lanza una excepcion.
     */
    virtual explicit operator std::string() const;
    virtual explicit operator int() const;
};

}

#endif
