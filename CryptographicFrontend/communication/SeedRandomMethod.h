#ifndef COMMUNICATION_SEEDRANDOMMETHOD_HPP
#define COMMUNICATION_SEEDRANDOMMETHOD_HPP
#include "Method.h"

namespace communication
{

class SeedRandomMethod : public Method
{
public:
    SeedRandomMethod ( std::string sessionHandler, std::string seed );
    virtual ~SeedRandomMethod() = default;
    virtual ResponseMessage parseResponse ( const Json::Value& value ) override;
};

}

#endif // SEEDRANDOMMETHOD_HPP
// kate: indent-mode cstyle; replace-tabs on; 
