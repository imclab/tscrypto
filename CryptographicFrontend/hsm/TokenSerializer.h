#ifndef TCBHSM_TOKENSERIALIZER_H
#define TCBHSM_TOKENSERIALIZER_H
#include <string>

namespace hsm
{

class Slot;

class CryptoObject;
class Token;

struct TokenSerializer
{
    virtual ~TokenSerializer() = default;
    virtual void saveToken ( Token & token ) = 0;
    virtual Token * getToken ( std::string label ) = 0;
    virtual void saveCryptoObject( Token & token, CryptoObject & object) = 0;
};

}

#endif // TCBHSM_TOKENSERIALIZER_H
