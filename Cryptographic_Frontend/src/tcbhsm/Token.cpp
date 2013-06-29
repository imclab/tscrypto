/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "Token.h"

#include <stdexcept>
#include <cstring>

using namespace tcbhsm;

Token::Token(std::string label, std::string pin)
: userPin_(pin), soPin_(pin) {
  if (label.size() <= 32)
    label_ = label;
  else
    throw std::runtime_error("Etiqueta con mas de 32 caracteres");
}

Token::~Token() {
  
}

void Token::getInfo(CK_TOKEN_INFO_PTR pInfo) const {
  if (!pInfo)
    throw new std::runtime_error("Puntero nulo pasado a getInfo.");
  
  if (label_.empty()) {
    memset(pInfo->label, ' ', 32);
  } else {
    char label[32];
    memset(label, ' ', 32);
    memcpy(label, label_.c_str(), label_.size());
    memcpy(pInfo->label, label, 32);
  }
  
  // Copiado descaradamente de SoftHSM
  memset(pInfo->manufacturerID, ' ', 32);
  memset(pInfo->model, ' ', 16);
  memset(pInfo->serialNumber, ' ', 16);
  
  memcpy(pInfo->manufacturerID, "NicLabs", 7);
  memcpy(pInfo->model, "tcbhsm", 6);
  memcpy(pInfo->serialNumber, "1", 1);

  pInfo->flags = currentSlot->tokenFlags;
  pInfo->ulMaxSessionCount = MAX_SESSION_COUNT;
  pInfo->ulSessionCount = softHSM->getSessionCount();
  pInfo->ulMaxRwSessionCount = MAX_SESSION_COUNT;
  pInfo->ulRwSessionCount = softHSM->getSessionCount();
  pInfo->ulMaxPinLen = MAX_PIN_LEN;
  pInfo->ulMinPinLen = MIN_PIN_LEN;
  pInfo->ulTotalPublicMemory = CK_UNAVAILABLE_INFORMATION;
  pInfo->ulFreePublicMemory = CK_UNAVAILABLE_INFORMATION;
  pInfo->ulTotalPrivateMemory = CK_UNAVAILABLE_INFORMATION;
  pInfo->ulFreePrivateMemory = CK_UNAVAILABLE_INFORMATION;
  pInfo->hardwareVersion.major = VERSION_MAJOR;
  pInfo->hardwareVersion.minor = VERSION_MINOR;
  pInfo->firmwareVersion.major = VERSION_MAJOR;
  pInfo->firmwareVersion.minor = VERSION_MINOR;

  time_t rawtime;
  time(&rawtime);
  char dateTime[17];
  strftime(dateTime, 17, "%Y%m%d%H%M%S00", gmtime(&rawtime));
  memcpy(pInfo->utcTime, dateTime, 16);
}