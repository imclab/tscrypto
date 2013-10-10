/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef TCBHSM_TOKEN_H
#define TCBHSM_TOKEN_H

#include "config.h"
#include <string>
#include <memory>

#include "cryptoki.h"

namespace tcbhsm
{
class Token
{
public:
  enum class SecurityLevel {
    SECURITY_OFFICER, USER, PUBLIC
  };

  Token(std::string label, std::string pin);
  Token(std::string label, std::string userPin, std::string soPin);
  void getInfo(CK_TOKEN_INFO_PTR pInfo) const;
  void setUserPin(std::string pin);
  bool isInited() const;
  void login(CK_USER_TYPE userType, CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen);
  void logout();
  SecurityLevel getSecurityLevel() const;

private:
  CK_FLAGS tokenFlags_;
  std::string userPin_;
  std::string soPin_;
  std::string label_;
  SecurityLevel securityLevel_;
  bool loggedIn_;

protected:
  SecurityLevel checkUserPin(CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen) const;
  SecurityLevel checkSecurityOfficerPin(CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen) const;
};

using TokenPtr = std::unique_ptr<Token>;

}


#endif // TCBHSM_TOKEN_H
