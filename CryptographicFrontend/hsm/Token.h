/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef TCBHSM_TOKEN_H
#define TCBHSM_TOKEN_H

#include "config.h"

#include <map>
#include <string>
#include <memory>

#include "pkcs11.h"

namespace hsm
{

class Slot;
class Session;
class Slot;
class CryptoObject;
using CryptoObjectPtr = std::unique_ptr<CryptoObject>;
// Tokens are (unlimited) containers of crypto objects...
class Token
{
public:
    enum class SecurityLevel
    {
        SECURITY_OFFICER, USER, PUBLIC
    };

    Token ( std::string label, std::string userPin, std::string soPin);
    virtual ~Token();

    // Basic operations      
    virtual void getInfo ( CK_TOKEN_INFO_PTR pInfo ) const;
    virtual void setUserPin ( std::string pin );
    virtual bool isInited() const;
    virtual void login ( CK_USER_TYPE userType, CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen );
    virtual void logout();
    virtual SecurityLevel getSecurityLevel() const;
    virtual std::string getLabel() const;

    // Container operations
    virtual CK_OBJECT_HANDLE addObject ( CryptoObject * object );
    virtual CryptoObject & getObject ( CK_OBJECT_HANDLE handle );
    virtual std::map<CK_OBJECT_HANDLE, CryptoObjectPtr> & getObjects();    

protected:
    SecurityLevel checkUserPin ( CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen ) const;
    SecurityLevel checkSecurityOfficerPin ( CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen ) const;
    
private:
    CK_FLAGS tokenFlags_;
    std::string userPin_;
    std::string soPin_;
    std::string label_;
    SecurityLevel securityLevel_;
    bool loggedIn_;
//     int sessionCount_; // TODO
//     int rwSessionCount; // TODO

    std::map<CK_OBJECT_HANDLE, CryptoObjectPtr> objects_;
};

}


#endif // TCBHSM_TOKEN_H
