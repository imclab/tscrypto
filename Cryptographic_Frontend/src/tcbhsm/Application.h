/**
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef TCBHSM_APPLICATION_H
#define TCBHSM_APPLICATION_H

#include <vector>
#include <string>

#include "Session.h"
#include "Slot.h"
#include "pkcs11.h"

namespace tcbhsm
{
  
  class Application
  {
  public:
    Application();
    virtual ~Application();
    
    void errorLog(std::string message) const;
    const std::vector<Slot> & getSlotList() const;
    const Slot & getSlot(CK_SLOT_ID id) const; // throws exception
    const Session & getSession(CK_SESSION_HANDLE session) const; // throws exception
    
  private:
    std::ostream* out_;
    
    std::vector<Session> sessions_;
    std::vector<Slot> slots_;
    
  };
  
  
}

#endif // TCBHSM_APPLICATION_H
