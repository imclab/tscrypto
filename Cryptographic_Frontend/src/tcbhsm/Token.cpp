/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "Token.h"
#include "TcbError.h"

#include <cstring>

using namespace tcbhsm;

Token::Token(std::string label, std::string pin)
  : userPin_(pin), soPin_(pin), securityLevel_(SecurityLevel::PUBLIC)
{
  if (label.size() <= 32)
    label_ = label;
  else
    throw TcbError("Etiqueta con mas de 32 caracteres", CKR_ARGUMENTS_BAD);
}

Token::~Token()
{

}

void Token::getInfo(CK_TOKEN_INFO_PTR pInfo) const
{
  if (!pInfo)
    throw TcbError("Puntero nulo pasado a getInfo.", CKR_ARGUMENTS_BAD);

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

  pInfo->flags = tokenFlags_;
  pInfo->ulMaxSessionCount = MAX_SESSION_COUNT;
  pInfo->ulSessionCount = 1; // TODO!
  pInfo->ulMaxRwSessionCount = MAX_SESSION_COUNT;
  pInfo->ulRwSessionCount = 1; // TODO!
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

bool Token::isInited() const
{
  return true;
}

auto Token::getSecurityLevel() const -> SecurityLevel {
  return securityLevel_;
}

bool Token::checkUserPin(const std::string & pin) const {
  return (userPin_ == pin);

}
bool Token::checkSecurityOfficerPin(const std::string & pin) const {
  return (soPin_ == pin);
}

void Token::login(CK_USER_TYPE userType, CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen) {
  switch(userType) {
  case CKU_SO:
    if (checkSecurityOfficerPin(std::string(reinterpret_cast<char *>(pPin), ulPinLen))) {
        securityLevel_ = SecurityLevel::SECURITY_OFFICER;
    }
    break;
  case CKU_USER:
  case CKU_CONTEXT_SPECIFIC:
    if (checkUserPin(std::string(reinterpret_cast<char *>(pPin), ulPinLen))) {
        securityLevel_ = SecurityLevel::USER;
    }
    break;
  }
}

void Token::logout() {
  securityLevel_ = SecurityLevel::PUBLIC;
}
