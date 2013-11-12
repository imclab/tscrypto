/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef TCBHSM_SLOT_H
#define TCBHSM_SLOT_H

#include "config.h"
#include <string>
#include <vector>
#include <map>
#include <memory>

#include "cryptoki.h"

namespace hsm
{

class Application;

class ConnectionManager;

class Token;
class Session;
class Configuration;

using TokenPtr = std::unique_ptr<Token>;
using SessionPtr = std::unique_ptr<Session>;

/// Clase que representa un espacio para un token en el HSM
class Slot
{
public:
    Slot ( CK_SLOT_ID id, Application & application );
    ~Slot();

    CK_SESSION_HANDLE openSession ( CK_FLAGS flags, CK_VOID_PTR pApplication, CK_NOTIFY notify );
    void closeSession ( CK_SESSION_HANDLE handle );
    void closeAllSessions();
    Session & getSession ( CK_SESSION_HANDLE handle );
    bool hasSession ( CK_SESSION_HANDLE handle );
    CK_ULONG sessionsCount() const;

    Application const & getApplication() const;
    CK_SLOT_ID getId() const;
    void getInfo ( CK_SLOT_INFO_PTR pInfo ) const; // throws exception
    void insertToken ( Token * token );
    Token & getToken() const; // throws exception
    bool isTokenPresent() const;
private:
    CK_SLOT_ID slotId_;
    Application const & application_;
    CK_FLAGS slotFlags_;
    TokenPtr token_; // Esto por la posibilidad de no estar presente :)

    std::map<CK_SESSION_HANDLE, SessionPtr> sessions_;
};
}

#endif // TCBHSM_SLOT_H
