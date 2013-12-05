/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef HSM_DATABASE_H
#define HSM_DATABASE_H

#include <TokenSerializer.h>
#include "Configuration.h"
#include <sqlite3.h>

// sqlite3 serializer

namespace hsm {

class Database : public hsm::TokenSerializer
{
    // TODO: GET MAX CRYPTO OBJECT ID
    sqlite3 * db_;    
public:
    Database(Configuration::DatabaseConf const& conf);
    Database() = default;
    virtual ~Database();
    Database(Database &) = default;
    Database(Database &&) = default;
    
    Database & operator=(Database &) = default;
    Database & operator=(Database &&) = default;
    
    virtual void init(Configuration::DatabaseConf const& conf);
    virtual hsm::Token* getToken(std::string label);
    virtual void saveToken(hsm::Token& token);
    virtual void saveCryptoObject(Token& token, CryptoObject& object);
};
}

#endif // HSM_DATABASE_H
