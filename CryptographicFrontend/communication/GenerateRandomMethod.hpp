#ifndef GENERATERANDOMMETHOD_HPP
#define GENERATERANDOMMETHOD_HPP
#include "Method.hpp"

namespace communication
{

class GenerateRandomMethod : public Method
{
public:
    GenerateRandomMethod ( std::string sessionHandler, long int length );
    virtual ~GenerateRandomMethod() = default;
    virtual ResponseMessagePtr parseResponse ( const std::string & message ) override;
};

}

#endif
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
