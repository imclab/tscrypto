/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "Database.h"
#include "TcbError.h"

using namespace hsm;

Database::Database ( std::string filename )
{
    int rc = sqlite3_open_v2(filename.c_str(), &db_, 
                             SQLITE_OPEN_READWRITE | SQLITE_OPEN_CREATE, 
                             nullptr);
    if(rc != SQLITE_OK){
        throw TcbError("Database::Database", "Could not open database.", CKR_GENERAL_ERROR);
    }
    
}

Database::~Database()
{
    sqlite3_close_v2(db_);
}

void Database::createTable()
{
    
}

Token* Database::getToken ( const std::string& label )
{

}

void Database::saveToken ( Token& token )
{

}
