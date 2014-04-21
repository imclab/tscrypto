/*
Copyright 2013 NIC Chile Research Labs.
This file is part of PKCS11-TsCrypto.

PKCS11-TsCrypto is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

PKCS11-TsCrypto is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with PKCS11-TsCrypto.  If not, see <http://www.gnu.org/licenses/>.
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
    void init(std::string path);
    
public:
    Database(std::string path);
    Database() = default;
    virtual ~Database();
    Database(Database const &) = delete;
    Database(Database &&);    
    Database & operator=(Database const &) = delete;
    Database & operator=(Database &&);
    
    
    virtual hsm::Token* getToken(std::string label);
    virtual void saveToken(hsm::Token& token);
    virtual void saveCryptoObject(Token& token, CryptoObject& object);
};
}

#endif // HSM_DATABASE_H
