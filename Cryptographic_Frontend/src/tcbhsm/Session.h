/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef SESSION_H
#define SESSION_H

#include "cf/Connection.hpp"
#include "pkcs11t.h"
#include <vector>
namespace tcbhsm {
    class Session
    {
    private:
      Session &operator=(const Session &other) = delete;
      bool operator==(const Session &other) = delete;
      Session(const Session &other) = delete;
    public:
      Session();
      
      ~Session();
      
      
      long long getKeyHandle(CK_OBJECT_HANDLE hKey);
      Connection* createConnection(); // RAII connection
    };
}

#endif // SESSION_H
