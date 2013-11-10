/**
 * @file
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef Method_H_
#define Method_H_

#include <string>
#include <stdexcept>

#include "MethodMessage.hpp"


namespace communication
{
    
    class ResponseMessage;
    class Connection;
    using ResponseMessagePtr = std::unique_ptr<ResponseMessage>;
    using ResponseMessageStrategy = std::function<ResponseMessagePtr(std::string const &)>;
    
    
    class Method   // Por ahora los metodos devuelven un string json...
    {
    private:
        MethodMessage message_;
        std::string response_;
        ResponseMessagePtr responseMessage_;
    protected:    
        
        Method(const std::string & name);
        void addArgument(communication::IArgument* argument);
        virtual ResponseMessagePtr parseResponse(const std::string & message) = 0;
        
    public:
        Method & execute(Connection const & connection); // throw (ConnectionException);
        const ResponseMessage & getResponse();
        virtual ~Method() = default;
        
    };
    
}

#endif // Method_H_
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
