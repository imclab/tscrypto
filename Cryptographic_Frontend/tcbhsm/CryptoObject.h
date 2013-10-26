/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef TCBHSM_SESSIONOBJECT_H
#define TCBHSM_SESSIONOBJECT_H
#include "cryptoki.h"
#include <vector>
#include <memory>

namespace tcbhsm
{
  
  class Database;
  
  class CryptoObject
  {
  private:
    std::vector<CK_ATTRIBUTE> attributes_;
  public:
    CryptoObject (CK_ATTRIBUTE_PTR pAttributes, CK_ULONG ulCount);
    ~CryptoObject ();
    
    bool match (CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) const;
    void copyAttributes (CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) const; // throw Exception
    const CK_ATTRIBUTE * findAttribute (CK_ATTRIBUTE* attribute) const;
    
    std::vector<CK_ATTRIBUTE> const & getAttributes() const;
  };
  
}

#endif // TCBHSM_SESSIONOBJECT_H
