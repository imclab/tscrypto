/**
 * @author Francisco Cifuentes [francisco@niclabs.cl]
 */

#ifndef RESPONSEMESSAGE_H
#define RESPONSEMESSAGE_H

#include <memory>
#include <string>
#include <map>
#include <boost/any.hpp>
#include <functional>

namespace cf
{

class ResponseMessage;
typedef std::unique_ptr<ResponseMessage> ResponseMessagePtr;
typedef std::function<ResponseMessagePtr(std::string const &)> ResponseMessageStrategy;


class ResponseMessage
{
private:
    std::map<std::string, boost::any> values;

public:
    static ResponseMessagePtr responseMessageFactory(std::string message,
            ResponseMessageStrategy strategy);

    ResponseMessage();
    ResponseMessage(ResponseMessage & rm);
    ResponseMessage& operator=(ResponseMessage const& rm);

    void setValue(std::string name, boost::any value);
    template <typename T> // Escondo la implementacion con Boost...
    T getValue(std::string name) {
        boost::any value = values.at(name);
        return boost::any_cast<T>(value);
    }
};

}

#endif // RESPONSEMESSAGE_H
