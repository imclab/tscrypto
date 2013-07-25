#include "SessionObject.h"

using namespace tcbhsm;

SessionObject::SessionObject(CK_ATTRIBUTE_TYPE attribute)
  : attribute_(attribute)
{

}

SessionObject::~SessionObject()
{

}

auto // Probando sintaxis nueva
SessionObject::match(CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) const -> bool
{
  for (CK_ULONG i=0; i<ulCount; ++i) {
    //if(pTemplate[i] == attribute_)
    return true;
  }

  return false;
}
