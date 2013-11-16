/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef TCBHSM_SESSIONOBJECT_H
#define TCBHSM_SESSIONOBJECT_H
#include "cryptoki.h"
#include <vector>
#include <memory>

namespace hsm
{
enum class CryptoObjectType
{
    SESSION_OBJECT, TOKEN_OBJECT
};


class CryptoObject
{
private:
    CK_OBJECT_HANDLE handle_;
    CryptoObjectType type_;
    std::vector<CK_ATTRIBUTE> attributes_;
public:
    CryptoObject ( CK_ATTRIBUTE_PTR pAttributes,
                   CK_ULONG ulCount,
                   CryptoObjectType type );
    ~CryptoObject ();

    bool match ( CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount ) const;
    void copyAttributes ( CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount ) const; // throw Exception
    const CK_ATTRIBUTE * findAttribute ( const CK_ATTRIBUTE* tmpl ) const;
    CryptoObjectType getType() const;
    CK_OBJECT_HANDLE getHandle() const;

    std::vector<CK_ATTRIBUTE> const & getAttributes() const;
};

}

#endif // TCBHSM_SESSIONOBJECT_H
