/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef TCBHSM_TOKEN_H
#define TCBHSM_TOKEN_H

#include "config.h"

#include "cf/Connection.hpp"
#include "CryptoObject.h"

#include <map>
#include <set>
#include <string>
#include <memory>

#include "cryptoki.h"

namespace tcbhsm
{
  // Tokens are (unlimited) containers of crypto objects...
  class Token
  {
  public:
    enum class SecurityLevel {
      SECURITY_OFFICER, USER, PUBLIC
    };
    
    Token(std::string label, std::string userPin, std::string soPin);
    ~Token();
    void getInfo(CK_TOKEN_INFO_PTR pInfo) const;
    void setUserPin(std::string pin);
    bool isInited() const;
    void login(CK_USER_TYPE userType, CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen);
    void logout();
    SecurityLevel getSecurityLevel() const;
    
    // Container operations
    CK_OBJECT_HANDLE addTokenObject(CryptoObject * object);
    CK_OBJECT_HANDLE addSessionObject(CryptoObject * object);
    std::string const * addKeyAlias(std::string alias);
    void removeKeyAlias(std::string alias);
    
    void destroySessionObjects(cf::Connection const & connection);
    
    CryptoObject & getObject(CK_OBJECT_HANDLE handle);
    std::map<CK_OBJECT_HANDLE, CryptoObjectPtr> & getObjects(CK_OBJECT_HANDLE handle);
    std::map<CK_OBJECT_HANDLE, CryptoObjectPtr> & getTokenObjects();
    std::map<CK_OBJECT_HANDLE, CryptoObjectPtr> & getSessionObjects();
    
  private:
    CK_FLAGS tokenFlags_;
    std::string userPin_;
    std::string soPin_;
    std::string label_;
    SecurityLevel securityLevel_;
    bool loggedIn_;
    
    // Strategy: TokenObjectHandle > 0, SessionObjectHandle < 0.
    CK_OBJECT_HANDLE actualTokenObjectHandle_;
    CK_OBJECT_HANDLE actualSessionObjectHandle_;
    
    std::map<CK_OBJECT_HANDLE, CryptoObjectPtr> tokenObjects_;
    std::map<CK_OBJECT_HANDLE, CryptoObjectPtr> sessionObjects_;
    
    std::set<std::string> keySet_;
    
  protected:
    SecurityLevel checkUserPin(CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen) const;
    SecurityLevel checkSecurityOfficerPin(CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen) const;
  };
  
  using TokenPtr = std::unique_ptr<Token>;
  
}


#endif // TCBHSM_TOKEN_H
