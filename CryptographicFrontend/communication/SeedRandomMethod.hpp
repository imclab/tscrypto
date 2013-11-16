#ifndef SEEDRANDOMMETHOD_HPP
#define SEEDRANDOMMETHOD_HPP
#include "Method.hpp"

namespace communication
{

class SeedRandomMethod : public Method
{
public:
    SeedRandomMethod ( std::string sessionHandler, std::string seed );
    virtual ~SeedRandomMethod() = default;
    virtual ResponseMessage* parseResponse ( const Json::Value& value ) override;
};

}

#endif // SEEDRANDOMMETHOD_HPP
// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
