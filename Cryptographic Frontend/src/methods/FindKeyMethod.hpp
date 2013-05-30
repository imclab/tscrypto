#ifndef FINDKEYMETHOD_HPP
#define FINDKEYMETHOD_HPP
#include <Method.hpp>
namespace cf {

class FindKeyMethod : public Method
{
private:
    bool privateKey_;
protected:
    virtual ResponseMessageStrategy getStrategy() const override;
public:
    FindKeyMethod(std::string keyType, std::string id);
};

}

#endif // FINDKEYMETHOD_H
