/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef HSM_DATABASE_H
#define HSM_DATABASE_H

#include <TokenSerializer.h>

namespace hsm {

class Database : public hsm::TokenSerializer
{
public:
    virtual hsm::Token* getToken(const std::string& label);
    virtual void saveToken(hsm::Token& token);
};
}

#endif // HSM_DATABASE_H
