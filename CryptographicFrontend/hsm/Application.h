/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef TCBHSM_APPLICATION_H
#define TCBHSM_APPLICATION_H

#include "config.h"

#include <vector>
#include <string>
#include <memory>
#include <iosfwd>

#include "pkcs11.h"
#include "RabbitConnectionManager.h"

namespace hsm
{
class Slot;
class Session;
class Configuration;
class ConnectionManager;

using SlotPtr = std::unique_ptr<Slot>;

/** La aplicacion tiene slots y algunas funcionalidades de ayuda... **/
class Application
{
public:
    Application ( std::ostream& out );
    virtual ~Application();

    virtual Slot & getSlot ( CK_SLOT_ID id ) const; // throws exception
    virtual std::vector<SlotPtr> const & getSlotList() const;

    virtual Slot & getSessionSlot ( CK_SESSION_HANDLE handle );
    virtual Session & getSession ( CK_SESSION_HANDLE session ); // throws exception

    virtual Configuration const & getConfiguration() const; // throws exception
    virtual ConnectionManager const & getConnectionManager() const;
    virtual void errorLog ( std::string message ) const;

private:
    std::ostream& out_;

    Configuration configuration_;
    RabbitConnectionManager connectionManager_;

    std::vector<SlotPtr> slots_;

};
}

#endif // TCBHSM_APPLICATION_H
