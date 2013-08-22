/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "Application.h"
#include "TcbError.h"

using namespace tcbhsm;

// Obsoleta
#if 0
Application::Application()
  : out_(std::cerr)
{
    // TODO: Revisar los Slots
    // Por defecto solo 1.
    SlotPtr slot0(new Slot(0));
    slots_.push_back(std::move(slot0));
}
#endif 

Application::Application(std::ostream& out)
  : out_(out)
{
    // TODO: Revisar los Slots
    // Por defecto solo 1.
    SlotPtr slot0(new Slot(0));
    slots_.push_back(std::move(slot0));
}

void Application::errorLog(std::string message) const
{
  out_ << message << std::endl;
}

Session & Application::getSession(CK_SESSION_HANDLE session) const
{
  try {
    unsigned long i = static_cast<unsigned long>(session);
    Session & s = *(sessions_.at(i));
    s.retain();
    return s;
  } catch(...) {
    throw TcbError("Application::getSession : Mal indice de sesion.", CKR_SESSION_HANDLE_INVALID);
  }
}

const std::vector<SlotPtr> & Application::getSlotList() const
{
  return slots_;
}

Slot & Application::getSlot(CK_SLOT_ID id) const
{
  unsigned int i = (unsigned int) id;
  return *(slots_.at(i));
}

void Application::openSession(CK_SLOT_ID slotID, CK_FLAGS flags,
                              CK_VOID_PTR pApplication, CK_NOTIFY notify,
                              CK_SESSION_HANDLE_PTR phSession)
{
  CK_SESSION_HANDLE i = 0;
  
  Slot &slot = getSlot(slotID);
  for (SessionPtr& session: sessions_) {
    // De entre todos los espacios para hacer sesiones
    // se busca el que esté vacío.
    if (session == nullptr) {    
      session.reset(new Session(flags, pApplication, notify, slot));
      slotSessionsMap_[slotID].insert(i);
      *phSession = i;
      return;
    }
    ++i;
  }

  throw TcbError("Application::openSession", "No se pueden abrir mas sesiones", CKR_GENERAL_ERROR);
}

void Application::closeSession(CK_SESSION_HANDLE hSession) // throws
{
  unsigned int i = hSession;
  CK_SLOT_ID slotID = getSession(hSession).getCurrentSlot().getId();  
  // Se elimina la sesion con todo lo que tiene adentro
  try {
    sessions_.at(i).reset(nullptr);
    slotSessionsMap_[slotID].erase(i);
  } catch(...) {
    throw TcbError("Application::closeSession", "Mal indice de sesion", CKR_SESSION_HANDLE_INVALID);
  }
}

void Application::closeAllSessions(CK_SLOT_ID slotID)
{
  try {
    auto& slotSessions = slotSessionsMap_.at(slotID);
    for (auto session: slotSessions) {
      closeSession(session);
    }
    
    // Para eliminar cualquier basura...
    slotSessions.clear();
    
  } catch(std::exception &e) {
    throw TcbError("Application::closeAllSessions", e.what(), CKR_ARGUMENTS_BAD);
  }
}
