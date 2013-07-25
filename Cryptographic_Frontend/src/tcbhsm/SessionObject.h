/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef TCBHSM_SESSIONOBJECT_H
#define TCBHSM_SESSIONOBJECT_H
#include "cryptoki.h"

namespace tcbhsm
{
class SessionObject
{
private:
  CK_ATTRIBUTE_TYPE attribute_;
  SessionObject() = delete;
public:
  SessionObject(CK_ATTRIBUTE_TYPE attribute_);
  SessionObject( const SessionObject & other) = default;
  SessionObject & operator=(const SessionObject & other) = default;

  virtual ~SessionObject();
  bool match(CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) const;
};


}

#endif // TCBHSM_SESSIONOBJECT_H
