#ifndef FINDKEYMETHOD_HPP
#define FINDKEYMETHOD_HPP
#include <Method.hpp>
namespace cf {

class FindKeyMethod : public Method
{
public:
    FindKeyMethod(std::string keyType, std::string id);
    virtual ResponseMessageStrategy getStrategy() const;
};

}

#endif // FINDKEYMETHOD_H
