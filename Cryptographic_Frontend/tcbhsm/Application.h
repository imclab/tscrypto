/**
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef TCBHSM_APPLICATION_H
#define TCBHSM_APPLICATION_H

#include "config.h"

#include <array>
#include <vector>
#include <set>
#include <string>
#include <memory>
#include <iostream>

#include "Session.h"
#include "Slot.h"
#include "cryptoki.h"

namespace tcbhsm
{

  /** La aplicacion maneja sesiones y slots, y la relacion entre ellos **/
class Application
{
public:
  Application(std::ostream& out = std::cerr);

  void errorLog(std::string message) const;
  const std::vector<SlotPtr> & getSlotList() const;
  Slot & getSlot(CK_SLOT_ID id) const; // throws exception
  // No olvidar hacer un .release justo antes de dejar de ocuparlo
  Session & getSession(CK_SESSION_HANDLE session) const; // throws exception

  void openSession(CK_SLOT_ID slotID, CK_FLAGS flags,
                   CK_VOID_PTR pApplication, CK_NOTIFY Notify,
                   CK_SESSION_HANDLE_PTR phSession); // throws Exception
  void closeSession(CK_SESSION_HANDLE hSession); // throws Exception
  void closeAllSessions(CK_SLOT_ID slotID); // throws Exception

private:
  std::ostream& out_;

  std::array<SessionPtr, MAX_SESSION_COUNT> sessions_;
  std::vector<SlotPtr> slots_;
  
  std::map<CK_SLOT_ID, std::set<unsigned long>> slotSessionsMap_;
};


}

#endif // TCBHSM_APPLICATION_H
