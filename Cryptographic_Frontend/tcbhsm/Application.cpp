/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "tcbhsm.h"

#include <cstdlib> // getenv
#include <ostream>

using namespace tcbhsm;

namespace {
  inline unsigned long toLong(CK_SESSION_HANDLE h) {
    return static_cast<unsigned long>(h) - 1;
  }
  inline CK_SESSION_HANDLE toSessionHandle(unsigned long l) {
    return static_cast<CK_SESSION_HANDLE>(l) + 1;
  }
}

Application::Application(std::ostream& out)
: out_(out)
{
  
  // First, read and setup the configuration.
  char const * configPath = std::getenv("TCB_CONFIG_FILE");
  if (configPath == nullptr) {
    throw TcbError("Application::Application", 
                   "TCB_CONFIG_FILE environment variable hasn't setted yet", 
                   CKR_DEVICE_ERROR);    
  }  
  configuration_.reset(new Configuration(std::string(configPath)));
  
  // By design, we will have one slot per configured token.  
  // The tokens are owned by the slots.
  int i = 0;
  for (auto const & slotConf: configuration_->getSlotConf()) {
    TokenPtr token( new Token(slotConf.label, slotConf.userPin, slotConf.soPin) );    
    SlotPtr slot(new Slot(i, std::move(token)));
    slots_.push_back(std::move(slot));    
  }  
  
}

void Application::errorLog(std::string message) const
{
  out_ << message << std::endl;
}

Session & Application::getSession(CK_SESSION_HANDLE session) const
{
  try {
    unsigned long i = toLong(session);
    Session & s = *(sessions_.at(i));
    return s;
  } catch(...) {
    throw TcbError("Application::getSession", 
                   "Mal indice de sesion.", 
                   CKR_SESSION_HANDLE_INVALID);
  }
}

const std::vector<SlotPtr> & Application::getSlotList() const
{
  return slots_;
}

Slot & Application::getSlot(CK_SLOT_ID id) const
{
  unsigned int i = static_cast<unsigned int>(id);
  try {
    Slot &slot = *(slots_.at(i));
    return slot;
  } 
  catch (std::out_of_range &e) {
    throw TcbError("Application::getSlot", e.what(), CKR_SLOT_ID_INVALID);
  }
}

void Application::openSession(CK_SLOT_ID slotID, CK_FLAGS flags,
                              CK_VOID_PTR pApplication, CK_NOTIFY notify,
                              CK_SESSION_HANDLE_PTR phSession)
{  
  if (flags == 0)
    throw TcbError("Application::openSession" , "flags == 0", CKR_SESSION_PARALLEL_NOT_SUPPORTED);
  
  if (phSession == nullptr)
    throw TcbError("Application::openSession", "phSession == nullptr", CKR_ARGUMENTS_BAD);
  
  // NOTE: CK_INVALID_HANDLE == 0
  
  unsigned long i = 0;
  
  Slot &slot = getSlot(slotID);
  for (SessionPtr& session: sessions_) {
    // De entre todos los espacios para hacer sesiones
    // se busca el que esté vacío.
    if (session == nullptr) {    
      session.reset(new Session(flags, pApplication, notify, slot, *configuration_));
      slotSessionsMap_[slotID].insert(i);
      *phSession = toSessionHandle(i);
      return;
    }
    ++i;
  }
  
  throw TcbError("Application::openSession", "No se pueden abrir mas sesiones", CKR_GENERAL_ERROR);
}

void Application::closeSession(CK_SESSION_HANDLE hSession) // throws
{
  unsigned long i = toLong(hSession);
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
      closeSession(toSessionHandle(session));
    }
    
    // Para eliminar cualquier basura...
    slotSessions.clear();
    
  } catch(std::exception &e) {
    throw TcbError("Application::closeAllSessions", e.what(), CKR_SLOT_ID_INVALID);
  }
}

Configuration const & Application::getConfiguration() const { // throws exception
  return *configuration_;
}
