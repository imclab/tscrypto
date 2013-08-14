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
  // TODO: Agregar manera de saber que es un objeto almacenado de manera distribuida. IE. es una llave RSA.
private:
  std::vector<CK_ATTRIBUTE> attributes_;
  bool isDistributed_;
public:
  SessionObject (CK_ATTRIBUTE_PTR pAttributes, CK_ULONG ulCount);
  SessionObject (CK_ATTRIBUTE_PTR pAttributes, CK_ULONG ulCount, bool distributed);

  bool match (CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) const;
  void copyAttributes (CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) const; // throw Exception
  const CK_ATTRIBUTE * findAttribute (CK_ATTRIBUTE* attribute) const;
  bool isDistributed() const;
};


}

#endif // TCBHSM_SESSIONOBJECT_H
