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

namespace tcbhsm
{  
  class Slot;
  class Session;
  class Configuration;
  
  using SlotPtr = std::unique_ptr<Slot>;  
  using SessionPtr = std::unique_ptr<Session>;
  
  /** La aplicacion maneja sesiones y slots, y la relacion entre ellos **/
  class Application
  {
  public:
    Application(std::ostream& out);
    ~Application();
    
    void errorLog(std::string message) const;
    const std::vector<SlotPtr> & getSlotList() const;
    Slot & getSlot(CK_SLOT_ID id) const; // throws exception
    Session & getSession(CK_SESSION_HANDLE session) const; // throws exception
    
    void openSession(CK_SLOT_ID slotID, CK_FLAGS flags,
                     CK_VOID_PTR pApplication, CK_NOTIFY Notify,
                     CK_SESSION_HANDLE_PTR phSession); // throws Exception
    void closeSession(CK_SESSION_HANDLE hSession); // throws Exception
    void closeAllSessions(CK_SLOT_ID slotID); // throws Exception
    
    Configuration const & getConfiguration() const; // throws exception
    
  private:
    std::ostream& out_;
    
    std::unique_ptr<Configuration> configuration_;
    std::map<CK_SESSION_HANDLE, SessionPtr> sessions_;
    std::vector<SlotPtr> slots_;
    
    std::map<CK_SLOT_ID, std::set<unsigned long>> slotSessionsMap_;
  };
}

#endif // TCBHSM_APPLICATION_H
