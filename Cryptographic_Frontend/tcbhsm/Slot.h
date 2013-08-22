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
  Slot(CK_SLOT_ID id);

  CK_SLOT_ID getId() const;
  void addSession(unsigned int sessionHandler);
  std::vector<unsigned int> & getSessions();
  void getInfo(CK_SLOT_INFO_PTR pInfo) const; // throws exception
  Token & getToken() const; // throws exception
  bool tokenIsPresent() const;
private:
  CK_FLAGS slotFlags_;
  CK_SLOT_ID slotId_;
  std::unique_ptr<Token> token_; // Esto por la posibilidad de no estar presente :)
  std::vector<unsigned int> sessions_;
};

using SlotPtr = std::unique_ptr<Slot>;
}

#endif // TCBHSM_SLOT_H
