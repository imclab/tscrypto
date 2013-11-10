/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef TCBHSM_TOKEN_H
#define TCBHSM_TOKEN_H

#include "config.h"

#include <map>
#include <set>
#include <string>
#include <memory>

#include "cryptoki.h"

namespace communication {
  class Connection;
  using ConnectionPtr = std::unique_ptr<Connection>;
}

namespace hsm
{
  class Session;
  class Slot;
  class CryptoObject;
  using CryptoObjectPtr = std::unique_ptr<CryptoObject>;
  
  // Tokens are (unlimited) containers of crypto objects...
  class Token
  {
  public:
    enum class SecurityLevel {
      SECURITY_OFFICER, USER, PUBLIC
    };
    
    Token(std::string label, std::string userPin, std::string soPin, Slot& slot);
    ~Token();
    
    // Basic operations
    void getInfo(CK_TOKEN_INFO_PTR pInfo) const;
    void setUserPin(std::string pin);
    bool isInited() const;
    void login(CK_USER_TYPE userType, CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen);
    void logout();
    SecurityLevel getSecurityLevel() const;
    std::string getLabel() const;
    
    // Container operations
    CK_OBJECT_HANDLE addObject(CryptoObject * object);
    CryptoObject & getObject(CK_OBJECT_HANDLE handle);
    std::map<CK_OBJECT_HANDLE, CryptoObjectPtr> & getObjects();
    
  private:
    CK_FLAGS tokenFlags_;
    std::string userPin_;
    std::string soPin_;
    std::string label_;
    SecurityLevel securityLevel_;
    bool loggedIn_;
    Slot &slot_;
    
    std::map<CK_OBJECT_HANDLE, CryptoObjectPtr> objects_;   
    
  protected:
    SecurityLevel checkUserPin(CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen) const;
    SecurityLevel checkSecurityOfficerPin(CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen) const;
  };
  
}


#endif // TCBHSM_TOKEN_H
