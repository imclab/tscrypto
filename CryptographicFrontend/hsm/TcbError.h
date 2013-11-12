#ifndef TCBERROR_H
#define TCBERROR_H

#include "cryptoki.h"

#include <exception>
#include <string>

namespace hsm
{
class TcbError : public std::exception
{
public:
    TcbError() = delete;
    TcbError ( std::string error, CK_RV code );
    TcbError ( std::string who, std::string error, CK_RV code );
    virtual ~TcbError();
    virtual CK_RV getErrorCode() const;
    virtual const char* what() const throw() override;
private:
    std::string error_;
    const CK_RV code_;
};
}

#endif // TCBERROR_H
