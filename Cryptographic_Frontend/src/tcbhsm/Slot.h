/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef TCBHSM_SLOT_H
#define TCBHSM_SLOT_H

#include "config.h"
#include <string>
#include <list>
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
  virtual ~Slot();
  virtual CK_SLOT_ID getId() const;

  virtual void getInfo(CK_SLOT_INFO_PTR pInfo) const; // throws exception
  virtual Token & getToken() const; // throws exception

  virtual bool tokenIsPresent() const;
private:
  CK_FLAGS slotFlags_;
  CK_SLOT_ID slotId_;

  std::unique_ptr<Token> token_; // Esto por la posibilidad de no estar presente :)
};
}

#endif // TCBHSM_SLOT_H
