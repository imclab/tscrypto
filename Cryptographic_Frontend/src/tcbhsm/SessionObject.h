/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef TCBHSM_SESSIONOBJECT_H
#define TCBHSM_SESSIONOBJECT_H
#include "cryptoki.h"
#include <vector>

namespace tcbhsm
{

class SessionObject
{
private:
  std::vector<CK_ATTRIBUTE> attributes_;
public:
  SessionObject (CK_ATTRIBUTE_PTR pAttributes, CK_ULONG ulCount);

  bool match (CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) const;
  void copyAttributes (CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) const; // throw Exception
  const CK_ATTRIBUTE * findAttribute (CK_ATTRIBUTE* attribute) const;
};


}

#endif // TCBHSM_SESSIONOBJECT_H
