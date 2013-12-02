#ifndef TCBHSM_TOKENSERIALIZER_H
#define TCBHSM_TOKENSERIALIZER_H
#include <string>

namespace hsm
{
class Token;

class TokenSerializer
{
public:
    virtual ~TokenSerializer() = default;
    virtual void saveToken ( Token & token ) = 0;
    virtual Token * getToken ( std::string const & label ) = 0;
};
}

#endif // TCBHSM_TOKENSERIALIZER_H
