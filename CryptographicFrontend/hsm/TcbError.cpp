#include "TcbError.h"

using namespace hsm;

TcbError::TcbError ( std::string error, CK_RV code ) : error_ ( error ), code_ ( code )
{
}

TcbError::TcbError ( std::string who, std::string error, CK_RV code ) : error_ ( who + " : " + error ), code_ ( code )
{
}

TcbError::~TcbError()
{
}

CK_RV TcbError::getErrorCode() const
{
    return code_;
}

const char* TcbError::what() const throw()
{
    return error_.c_str();
}
