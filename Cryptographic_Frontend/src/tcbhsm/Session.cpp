/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "tcbhsm/Session.h"
#include "cf/RabbitConnection.hpp"

using namespace tcbhsm;

Session::Session(CK_FLAGS flags, CK_VOID_PTR pApplication, CK_NOTIFY notify, Slot & currentSlot)
  : refCount_(1), flags_(flags), application_(pApplication), notify_(notify), currentSlot_(currentSlot)
{

}

Session::~Session()
{

}

auto Session::createConnection() -> ConnectionPtr &&
{
  // TODO: crear algun tipo de sistema de configuración (Me tinca algo como
  // variables de entorno)...
  return std::move(ConnectionPtr(new cf::RabbitConnection("localhost", 5672, "", "rpc_queue", 1)));
}

void Session::retain()
{
    ++refCount_;
}

void Session::release()
{
    if (refCount_ > 0)
        --refCount_;
}

unsigned int Session::count()
{
    return refCount_;
}

void Session::getSessionInfo(CK_SESSION_INFO_PTR pInfo) const {
    if (pInfo != nullptr) {
        pInfo->slotID = currentSlot_.getId();
        pInfo->state = getState();
        pInfo->flags = getFlags();
        pInfo->ulDeviceError = 0;
    }
}

CK_STATE Session::getState() const {
  // TODO: Completar la semántica de lecto-escritura.
  switch (currentSlot_.getToken().getSecurityLevel()) {
  case Token::SecurityLevel::SECURITY_OFFICER:
    return CKS_RW_SO_FUNCTIONS;
  case Token::SecurityLevel::USER:
    return CKS_RW_USER_FUNCTIONS;

  default:
  case Token::SecurityLevel::PUBLIC:
    return CKS_RW_PUBLIC_SESSION;
  }
}

CK_FLAGS Session::getFlags() const {
    return flags_;
}

void Session::login(CK_USER_TYPE userType, CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen) {
  Token & token = currentSlot_.getToken();
  token.login(userType, pPin, ulPinLen);
}

void Session::logout() {
  currentSlot_.getToken().logout();
}
