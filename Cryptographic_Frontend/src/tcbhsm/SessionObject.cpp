#include "SessionObject.h"

using namespace tcbhsm;

SessionObject::SessionObject(CK_ATTRIBUTE_PTR pAttributes, CK_ULONG ulCount) {
  auto end = pAttributes + ulCount;
  for (auto it = pAttributes; it != end; ++it) {
      attributes_.push_back(*it);
  }

}

SessionObject::~SessionObject()
{

}

namespace {
  bool operator==(const CK_ATTRIBUTE& lhs, const CK_ATTRIBUTE& rhs) {
    return lhs.type == rhs.type && lhs.pValue == rhs.pValue && lhs.ulValueLen == rhs.ulValueLen;
  }
}

auto // Probando sintaxis nueva
SessionObject::match(CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) const -> bool
{
  // Busqueda por fuerza bruta, no se me ocurre una mejor estructura de dato
  // necesito saber mejor las restricciones de un objeto.
  auto end = pTemplate + ulCount;
  for(auto it = pTemplate; it != end; ++it) {
    bool found = false;
    for(auto& attribute: attributes_) {
      if (attribute == *it)
        found = true;
    }

    if (!found)
      return false;
  }

  return true;
}
