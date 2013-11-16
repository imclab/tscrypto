/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef HSM_DATABASE_H
#define HSM_DATABASE_H

#include <sqlite3.h>
#include <string>
#include "TokenSerializer.h"

namespace hsm
{

class Database : public TokenSerializer
{
private:
    sqlite3 * db_;
public:
    Database(std::string filename);
    virtual ~Database();
    
    void createTable();
    
    // Get a Token pointer to be managed from the client...
    virtual Token * getToken ( const std::string& label );
    virtual void saveToken ( Token& token );
};

}

#endif // HSM_DATABASE_H
