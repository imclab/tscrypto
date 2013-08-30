#include "SessionObject.h"
#include "TcbError.h"

#include <cstdlib>
#include <cstring>

using namespace tcbhsm;

SessionObject::SessionObject(CK_ATTRIBUTE_PTR pAttributes, CK_ULONG ulCount) : isDistributed_(false) {
  CK_ATTRIBUTE att;
  auto end = pAttributes + ulCount;
  for (auto it = pAttributes; it != end; ++it) {
    att.type = it->type;
    att.ulValueLen = it->ulValueLen;
    att.pValue = malloc(att.ulValueLen);
    std::memcpy(att.pValue, it->pValue, att.ulValueLen);
    
    attributes_.push_back(att);
  }
}

SessionObject::SessionObject(CK_ATTRIBUTE_PTR pAttributes, CK_ULONG ulCount, bool distributed) : isDistributed_(distributed) {
  CK_ATTRIBUTE att;
  auto end = pAttributes + ulCount;
  for (auto it = pAttributes; it != end; ++it) {
    att.type = it->type;
    att.ulValueLen = it->ulValueLen;
    att.pValue = std::malloc(att.ulValueLen);
    std::memcpy(att.pValue, it->pValue, att.ulValueLen);
    
    attributes_.push_back(att);
  }
}

SessionObject::~SessionObject() {
  for (auto &attribute: attributes_) {
    std::free(attribute.pValue);
  }
}

namespace {
  bool operator==(const CK_ATTRIBUTE& lhs, const CK_ATTRIBUTE& rhs) {
    // return lhs.type == rhs.type && lhs.pValue == rhs.pValue && lhs.ulValueLen == rhs.ulValueLen;
    return lhs.type == rhs.type;
  }
}

bool SessionObject::match(CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) const {

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

const CK_ATTRIBUTE* SessionObject::findAttribute (CK_ATTRIBUTE* tmpl) const {
  for(auto& attribute: attributes_) {
    if (attribute.type == tmpl->type)
      return &attribute;
  }

  return nullptr;
}

namespace {

void copyAttribute (CK_ATTRIBUTE const* src, CK_ATTRIBUTE* dst) {
  if (dst == nullptr) // Esto no deberia pasar...
    throw TcbError("copyAttribute", "Atributo no encontrado", CKR_ATTRIBUTE_TYPE_INVALID);

  if (src == nullptr) {
    CK_LONG* ulValueLen = reinterpret_cast<CK_LONG*>(&(dst->ulValueLen));
    *ulValueLen = -1;
  }
    

  if (dst->ulValueLen >= src->ulValueLen) {
    dst->type = src->type;
    dst->pValue = src->pValue;
    dst->ulValueLen = src->ulValueLen;
  } else {
    CK_LONG* ulValueLen = reinterpret_cast<CK_LONG*>(&(dst->ulValueLen));
    *ulValueLen = -1;
    throw TcbError("copyAttribute", "Espacio no suficiente", CKR_BUFFER_TOO_SMALL);
  }

}

}

void SessionObject::copyAttributes(CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) const {
  if (pTemplate == nullptr)
    throw TcbError("SessionObject::copyAttributes", "pTemplate == nullptr", CKR_ARGUMENTS_BAD);
    
  for(CK_ULONG i=0; i<ulCount; ++i) {
    CK_ATTRIBUTE* ptr = &(pTemplate[i]);
    copyAttribute(findAttribute(ptr), ptr);
  }
}

bool SessionObject::isDistributed() const {
  return isDistributed_;
}

