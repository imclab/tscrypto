/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "tcbhsm.h"

#include <cstring> // memset, memcpy
// TODO: Replace memset and memcpy with std::fill and std::copy...

using namespace tcbhsm;

Slot::Slot(CK_SLOT_ID id)
: slotId_(id)
{
  
}

Slot::~Slot() {
  
}

CK_SESSION_HANDLE 
Slot::openSession(CK_FLAGS flags, CK_VOID_PTR pApplication, 
                  CK_NOTIFY notify, Configuration const & configuration)
{
  if (!isTokenPresent()) {
    throw TcbError("Slot::openSession",
                   "Token not present",
                   CKR_TOKEN_NOT_PRESENT);
  }
  
  Session * sessionPtr = new Session(flags, pApplication, 
                                     notify, *this, 
                                     configuration);
  
  CK_SESSION_HANDLE handle = sessionPtr->getHandle();  
  sessions_[handle].reset(sessionPtr);
  
  return handle;  
}

void Slot::closeSession(CK_SESSION_HANDLE handle)
{
  if (!isTokenPresent()) {
    throw TcbError("Slot::openSession",
                   "Token not present",
                   CKR_TOKEN_NOT_PRESENT);
  }
  
  if (sessions_.erase(handle) == 0) {
    throw TcbError("Slot::closeSession",
                   "Session handle doesn't exists in this slot",
                   CKR_SESSION_HANDLE_INVALID);
  }
}

void Slot::closeAllSessions()
{
  if (!isTokenPresent()) {
    throw TcbError("Slot::openSession",
                   "Token not present",
                   CKR_TOKEN_NOT_PRESENT);
  }
  
  sessions_.clear();  
}

Session & Slot::getSession(CK_SESSION_HANDLE handle)
{
  if (!isTokenPresent()) {
    throw TcbError("Slot::openSession",
                   "Token not present",
                   CKR_TOKEN_NOT_PRESENT);
  }
  
  try {
    return *(sessions_.at(handle));
  } catch (...) {
    throw TcbError("Slot::getSession",
                   "Session handle doesn't exists in this slot",
                   CKR_SESSION_HANDLE_INVALID);
  }
}

bool Slot::hasSession(CK_SESSION_HANDLE handle) 
{ 
  return sessions_.count(handle) > 0;
}

CK_ULONG Slot::sessionsCount() const
{
  return sessions_.size();
}

CK_SLOT_ID Slot::getId() const
{
  return slotId_;
}

void Slot::getInfo(CK_SLOT_INFO_PTR pInfo) const
{
  if (pInfo == NULL) {
    throw TcbError("El puntero pasado a getInfo es NULL.", CKR_ARGUMENTS_BAD);
  }

  std::memset(pInfo->slotDescription, ' ', 64);
  std::memset(pInfo->manufacturerID, ' ', 32);

  std::memcpy(pInfo->slotDescription, "Slot de TCBHSM", 14);
  std::memcpy(pInfo->manufacturerID, "NicLabs", 7);

  pInfo->flags = slotFlags_;

  pInfo->hardwareVersion.major = VERSION_MAJOR;
  pInfo->hardwareVersion.minor = VERSION_MINOR;
  pInfo->firmwareVersion.major = VERSION_MAJOR;
  pInfo->firmwareVersion.minor = VERSION_MINOR;
}

bool Slot::isTokenPresent() const
{
  return token_ != nullptr;
}

Token & Slot::getToken() const
{
  if(isTokenPresent())
    return *token_;
  else
    throw TcbError("El token no esta presente.", CKR_TOKEN_NOT_PRESENT);
}

void Slot::insertToken(TokenPtr token)
{
  token_ = std::move(token);
}