/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
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
