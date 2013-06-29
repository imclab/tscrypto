/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "Slot.h"

#include <stdexcept>
#include <cstring> // memset, memcpy

using namespace tcbhsm;

Slot::Slot(CK_SLOT_ID id) 
: slotId_(id), token_(new Token(""))
{
  
}

Slot::~Slot() {
  
}

CK_SLOT_ID Slot::getSlotId() const {
  return slotId_;
}

void Slot::getInfo(CK_SLOT_INFO_PTR pInfo) const {
  if (pInfo == NULL) {
    throw std::runtime_error("El puntero pasado a getInfo es NULL.");
  }
  
  std::memset(pInfo->slotDescription, ' ', 64);
  std::memset(pInfo->manufacturerID, ' ', 32);
  
  std::memcpy(pInfo->slotDescription, "Slot de tcbhsm", 14);
  std::memcpy(pInfo->manufacturerID, "NicLabs", 7)
  
  pInfo->flags = slotFlags_;
  // TODO: Que esto no esté harcodeado...
  pInfo->hardwareVersion.major = 0;
  pInfo->hardwareVersion.minor = 1;
  pInfo->firmwareVersion.major = 0;
  pInfo->firmwareVersion.minor = 1;
}

bool Slot::tokenIsPresent() const {
  return static_cast<bool>(token_);
}

const Token & getToken() const {
  if(tokenIsPresent())
    return *token_;
  else
    throw std::runtime_error("El token no esta presente.");
}