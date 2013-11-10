/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef TCBHSM_APPLICATION_H
#define TCBHSM_APPLICATION_H

#include "config.h"

#include <vector>
#include <set>
#include <map>
#include <string>
#include <memory>
#include <iosfwd>

#include "cryptoki.h"

namespace communication {
class Connection;
}

namespace hsm
{  
  class Slot;
  class Session;
  class Configuration;
  class ConnectionManager;
  
  using SlotPtr = std::unique_ptr<Slot>;  
  using SessionPtr = std::unique_ptr<Session>;
  
  /** La aplicacion tiene slots y algunas funcionalidades de ayuda... **/
  class Application
  {
  public:
    Application(std::ostream& out);
    ~Application();
    
    Slot & getSlot(CK_SLOT_ID id) const; // throws exception
    std::vector<SlotPtr> const & getSlotList() const;
    
    Slot & getSessionSlot(CK_SESSION_HANDLE handle);    
    Session & getSession(CK_SESSION_HANDLE session); // throws exception

    Configuration const & getConfiguration() const; // throws exception    
    ConnectionManager const & getConnectionManager() const;
    void errorLog(std::string message) const;    
    
  private:
    std::ostream& out_;
    
    std::unique_ptr<Configuration> configuration_;
    
    // TODO: revisar si es necesario crear una conexion nueva cada vez o se puede mantener una abierta...
    std::unique_ptr<ConnectionManager> connectionManager_;
    
    std::vector<SlotPtr> slots_;  
    
  };
}

#endif // TCBHSM_APPLICATION_H
