/*
   Copyright 2013 NIC Chile Research Labs.
   This file is part of PKCS11-TsCrypto.

   PKCS11-TsCrypto is free software: you can redistribute it and/or modify
   it under the terms of the GNU Lesser General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   PKCS11-TsCrypto is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public License
   along with PKCS11-TsCrypto.  If not, see <http://www.gnu.org/licenses/>.

*/

#include <cstdlib>
#include <cstring>

#include <json/json.h>
#include <base64.h>

#include "CryptoObject.h"
#include "TcbError.h"
#include "pkcs11.h"


using namespace hsm;

CK_OBJECT_HANDLE CryptoObject::actualHandle = 0;

using AttributeMapPair = std::pair<CK_ATTRIBUTE_TYPE, CK_ATTRIBUTE>;


// NOTE: The attributes are copied into de CryptoObject, 
// to make memory management easier.
CryptoObject::CryptoObject ( CK_ATTRIBUTE_PTR pAttributes,
    CK_ULONG ulCount,
    CryptoObjectType type )
: handle_ ( ++actualHandle ), type_ ( type )
{
  CK_ATTRIBUTE att;
  auto end = pAttributes + ulCount;
  for ( auto it = pAttributes; it != end; ++it ) {
    att.type = it->type;
    att.ulValueLen = it->ulValueLen;
    att.pValue = malloc ( att.ulValueLen );
    std::memcpy ( att.pValue, it->pValue, att.ulValueLen );

    attributes_[att.type] = att;
  }
}

  CryptoObject::CryptoObject(CK_OBJECT_HANDLE handle, std::vector< CK_ATTRIBUTE > attributes )
: handle_(handle), type_(CryptoObjectType::TOKEN_OBJECT)
{
  for (CK_ATTRIBUTE att: attributes) {
    attributes_[att.type] = att;
  }
}


CryptoObject::~CryptoObject()
{
  for ( AttributeMapPair const & attribute: attributes_ ) {
    std::free ( attribute.second.pValue );
  }
}

bool CryptoObject::match ( CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount ) const
{       
  for (CK_ULONG i = 0; i<ulCount; i++) {
    CK_ATTRIBUTE templateAttr = pTemplate[i];
    auto attrIt = attributes_.find(templateAttr.type);	

    if(attrIt == attributes_.end()) {
      return false;
    } else { // Verify that are a byte-to-byte match...
      CK_ATTRIBUTE attr = attrIt->second;
      for ( CK_ULONG j = 0; j < attr.ulValueLen; ++j ) {
        if ( reinterpret_cast<char*>(attr.pValue)[j] != reinterpret_cast<char*>(templateAttr.pValue)[j] ) {
          return false;
        }
      }
    }
  }

  return true;
}

CK_ATTRIBUTE const * CryptoObject::findAttribute ( CK_ATTRIBUTE const * tmpl ) const
{
  if(attributes_.count(tmpl->type) > 0) {
    return &(attributes_.at(tmpl->type));
  } else {   
    return nullptr;
  }
}

namespace
{

  void copyAttribute ( CK_ATTRIBUTE const* src, CK_ATTRIBUTE* dst )
  {
    if ( dst->pValue == nullptr ) {
      dst->ulValueLen = src->ulValueLen;
      return;
    }

    if ( dst->ulValueLen >= src->ulValueLen ) {
      dst->type = src->type;
      dst->ulValueLen = src->ulValueLen;
      std::memcpy(dst->pValue, src->pValue, src->ulValueLen);
    } else {
      *(CK_LONG*)(dst->ulValueLen) = dst->ulValueLen;
      throw TcbError ( "copyAttribute", "Espacio no suficiente", CKR_BUFFER_TOO_SMALL );
    }

  }

}

void CryptoObject::copyAttributes ( CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount ) const
{
  if ( pTemplate == nullptr ) {
    throw TcbError ( "CryptoObject::copyAttributes", "pTemplate == nullptr", CKR_ARGUMENTS_BAD );
  }

  for ( CK_ULONG i=0; i<ulCount; ++i ) {
    CK_ATTRIBUTE * dst = & ( pTemplate[i] );
    CK_ATTRIBUTE const* src = findAttribute ( dst );
    if ( src != nullptr ) {
      copyAttribute ( src, dst );
    } else {
      throw TcbError ( "CryptoObject::copyAttributes", "src == nullptr", CKR_ARGUMENTS_BAD );
    }
  }
}

CryptoObjectType CryptoObject::getType() const
{
  return type_;
}

CK_OBJECT_HANDLE CryptoObject::getHandle() const
{
  return handle_;
}


const CryptoObject::AttributeMap& CryptoObject::getAttributes() const
{
  return attributes_;
}
