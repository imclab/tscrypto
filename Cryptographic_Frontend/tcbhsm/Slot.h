/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef TCBHSM_SLOT_H
#define TCBHSM_SLOT_H

#include "config.h"
#include <string>
#include <vector>
#include <memory>

#include "Token.h"
#include "cryptoki.h"

namespace tcbhsm
{

/// Clase que representa un espacio para un token en el HSM
class Slot
{
public:
  Slot(CK_SLOT_ID id, TokenPtr token);
  
  CK_SLOT_ID getId() const;
  void getInfo(CK_SLOT_INFO_PTR pInfo) const; // throws exception
  void initToken(std::string label, std::string pin);
  Token & getToken() const; // throws exception
  bool tokenIsPresent() const;
private:
  CK_FLAGS slotFlags_;
  CK_SLOT_ID slotId_;
  TokenPtr token_; // Esto por la posibilidad de no estar presente :)
};

using SlotPtr = std::unique_ptr<Slot>;
}

#endif // TCBHSM_SLOT_H
