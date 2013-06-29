/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef TCBHSM_TOKEN_H
#define TCBHSM_TOKEN_H

#include <string>

namespace tcbhsm {
  class Token {
  public:
    Token(std::string label, std::string pin);
    virtual ~Token();
    virtual void getInfo(CK_TOKEN_INFO_PTR pInfo) const;
    
  private:
    CK_FLAGS tokenFlags_;
    std::string userPin_;
    std::string soPin_;
    std::string label_;
  };
}


#endif // TCBHSM_TOKEN_H