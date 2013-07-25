/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "Application.h"

#include <iostream>
#include <stdexcept>

using namespace tcbhsm;

Application::Application()
  : out_(&std::cerr)
{
    // TODO: Revisar los Slots
    // Por defecto solo 1.
    SlotPtr slot0(new Slot(0));
    slots_.push_back(std::move(slot0));
}

Application::~Application()
{

}

void Application::errorLog(std::string message) const
{
  (*out_) << message << std::endl;
}

Session & Application::getSession(CK_SESSION_HANDLE session) const
{
  unsigned long i = static_cast<unsigned long>(session);
  Session & s = *(sessions_.at(i));
  s.retain();
  return s;
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
  int i = 0;
  for (SessionPtr& session: sessions_) {
    // De entre todos los espacios para hacer sesiones
    // se busca el que esté vacío.
    if (session == nullptr) {
      session.reset(new Session(flags, pApplication, notify, *(slots_.at(slotID))));
      *phSession = i;
      return;
    }
    ++i;
  }

  throw std::runtime_error("No se pueden abrir mas sesiones.");
}

void Application::closeSession(CK_SESSION_HANDLE hSession)
{
  int i = hSession;
  // Se elimina la sesion con todo lo que tiene adentro
  sessions_.at(i).reset(nullptr);
}
