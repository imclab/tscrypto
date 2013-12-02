/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#include <Token.h>
#include "CryptoObject.h"
#include "Database.h"


using namespace hsm;

hsm::Token* Database::getToken(const std::string& label)
{

}

void Database::saveToken(hsm::Token& token)
{
    std::string label (token.getLabel());
    for (auto & pair : token.getObjects()) {
	CK_OBJECT_HANDLE handle = pair.first;
	for (CK_ATTRIBUTE const & attribute : pair.second->getAttributes()) {
	    // TODO!
	    switch(attribute.type) {
		case CKA_CLASS:
		    break;
		case CKA_TOKEN:
		    break;
		case CKA_PRIVATE:
		    break;
		case CKA_LABEL:
		    break;
	    }
	}
    }
}
