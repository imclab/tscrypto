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

#ifndef TCBHSM_SESSIONOBJECT_H
#define TCBHSM_SESSIONOBJECT_H
#include "pkcs11.h"
#include <vector>
#include <unordered_map>

namespace hsm
{

enum class CryptoObjectType
{
    SESSION_OBJECT, TOKEN_OBJECT
};


class CryptoObject
{
    using AttributeMap = std::unordered_map<CK_ATTRIBUTE_TYPE, CK_ATTRIBUTE>;    
    CK_OBJECT_HANDLE handle_;
    CryptoObjectType type_;
    
    AttributeMap attributes_;
    // std::vector<CK_ATTRIBUTE> attributes_;
    
public:
    CryptoObject ( CK_ATTRIBUTE_PTR pAttributes,
                   CK_ULONG ulCount,
                   CryptoObjectType type );
    
    // For deserialization... Shallow copy of attributes (owns CK_ATTRIBUTE.pValue)
    CryptoObject ( CK_OBJECT_HANDLE handle, std::vector<CK_ATTRIBUTE> attributes );
    ~CryptoObject ();

    virtual bool match ( CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount ) const;
    virtual void copyAttributes ( CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount ) const; // throw Exception
    virtual const CK_ATTRIBUTE * findAttribute ( const CK_ATTRIBUTE* tmpl ) const;
    virtual CryptoObjectType getType() const;
    virtual CK_OBJECT_HANDLE getHandle() const;
    
    virtual AttributeMap const & getAttributes() const;
    
    static CK_OBJECT_HANDLE actualHandle;
};

}

#endif // TCBHSM_SESSIONOBJECT_H
