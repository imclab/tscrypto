/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "Slot.h"

#include "TcbError.h"
#include <cstring> // memset, memcpy

using namespace tcbhsm;

Slot::Slot(CK_SLOT_ID id)
  : slotId_(id), token_(new Token("", "1234"))
{

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

  std::memcpy(pInfo->slotDescription, "Slot de tcbhsm", 14);
  std::memcpy(pInfo->manufacturerID, "NicLabs", 7);

  pInfo->flags = slotFlags_;

  pInfo->hardwareVersion.major = VERSION_MAJOR;
  pInfo->hardwareVersion.minor = VERSION_MINOR;
  pInfo->firmwareVersion.major = VERSION_MAJOR;
  pInfo->firmwareVersion.minor = VERSION_MINOR;
}

bool Slot::tokenIsPresent() const
{
  return static_cast<bool>(token_);
}

Token & Slot::getToken() const
{
  if(tokenIsPresent())
    return *token_;
  else
    throw TcbError("El token no esta presente.", CKR_SLOT_ID_INVALID);
}
