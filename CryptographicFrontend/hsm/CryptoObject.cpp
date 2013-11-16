#include <cstdlib>
#include <cstring>

#include <json/json.h>
#include <base64/base64.h>

#include "CryptoObject.h"
#include "TcbError.h"
#include "cryptoki/cryptoki.h"


using namespace hsm;

namespace
{
CK_OBJECT_HANDLE actualHandle = 0;
}

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

        attributes_.push_back ( att );
    }
}

CryptoObject::~CryptoObject()
{
    for ( auto &attribute: attributes_ ) {
        std::free ( attribute.pValue );
    }
}

bool CryptoObject::match ( CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount ) const
{
    // Busqueda por fuerza bruta, no se me ocurre una mejor estructura de dato
    // necesito saber mejor las restricciones de un objeto.
    
    for (CK_ULONG i = 0; i<ulCount; i++) {
        bool found = false;
        for (CK_ATTRIBUTE const & attribute: attributes_) {
            if (attribute.type == pTemplate[i].type) {
                found = true;
            }
        }
        
        if ( !found ) {
            return false;
        }
    }
    
    return true;
}

CK_ATTRIBUTE const * CryptoObject::findAttribute ( CK_ATTRIBUTE const * tmpl ) const
{
    for ( auto& attribute: attributes_ ) {
        if ( attribute.type == tmpl->type ) {
            return &attribute;
        }
    }

    return nullptr;
}

namespace
{

void copyAttribute ( CK_ATTRIBUTE const* src, CK_ATTRIBUTE* dst )
{
    if ( dst == nullptr ) { // Esto no deberia pasar...
        throw TcbError ( "copyAttribute", "Atributo no encontrado", CKR_ATTRIBUTE_TYPE_INVALID );
    }

    if ( src == nullptr ) {
        CK_LONG* ulValueLen = reinterpret_cast<CK_LONG*> ( & ( dst->ulValueLen ) );
        *ulValueLen = -1;
    }


    if ( dst->ulValueLen >= src->ulValueLen ) {
        dst->type = src->type;
        dst->pValue = src->pValue;
        dst->ulValueLen = src->ulValueLen;
    } else {
        CK_LONG* ulValueLen = reinterpret_cast<CK_LONG*> ( & ( dst->ulValueLen ) );
        *ulValueLen = -1;
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
        CK_ATTRIBUTE* ptr = & ( pTemplate[i] );
        const CK_ATTRIBUTE* src = findAttribute ( ptr );
        if ( src != nullptr ) {
            copyAttribute ( src, ptr );
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


std::vector<CK_ATTRIBUTE> const & CryptoObject::getAttributes() const
{
    return attributes_;
}
