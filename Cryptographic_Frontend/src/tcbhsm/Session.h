/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef SESSION_H
#define SESSION_H

#include "config.h"
#include "cf/Connection.hpp"
#include "cryptoki.h"

#include <vector>
#include <memory>
#include "SessionObject.h"
#include "Slot.h"
namespace tcbhsm
{
using SessionObjectPtr = std::unique_ptr<SessionObject>;
using ConnectionPtr = std::unique_ptr<cf::Connection>;
//using SlotPtr = std::shared_ptr<Slot>;

class Session
{
public:
  Session(CK_FLAGS flags, CK_VOID_PTR pApplication, CK_NOTIFY notify, Slot & currentSlot);
  ~Session();

  CK_STATE getState() const;
  CK_FLAGS getFlags() const;

  void getSessionInfo(CK_SESSION_INFO_PTR pInfo) const;

  void login(CK_USER_TYPE userType, CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen);
  void logout();

  ConnectionPtr && createConnection(); // RAII connection, rvalue reference

  // Reference counting;
  void retain();
  void release();
  unsigned int count();

private:
  Session & operator=(const Session & other) = delete;
  bool operator==(const Session & other) = delete;
  Session(const Session & other) = delete;
  Session() = delete;

  std::vector<SessionObjectPtr> objects_;
  unsigned int refCount_;
  const CK_FLAGS flags_;
  const CK_VOID_PTR application_;
  const CK_NOTIFY notify_;
  Slot & currentSlot_;

};
}

#endif // SESSION_H
