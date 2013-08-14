/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef TCBHSM_TOKEN_H
#define TCBHSM_TOKEN_H

#include "config.h"
#include <string>

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
  void getInfo(CK_TOKEN_INFO_PTR pInfo) const;
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

protected:
  bool checkUserPin(const std::string & pin) const;
  bool checkSecurityOfficerPin(const std::string & pin) const;
};
}


#endif // TCBHSM_TOKEN_H
