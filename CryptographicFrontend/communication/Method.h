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

#ifndef COMMUNICATION_METHOD_H
#define COMMUNICATION_METHOD_H

#include <string>
#include <stdexcept>

#include "MethodMessage.h"
#include "ResponseMessage.h"

namespace Json
{
class Value;
}


namespace communication
{

class Connection;
class Method
{
private:
    MethodMessage message_;
    ResponseMessage responseMessage_;
protected:
    Method ( const std::string & name );

    virtual void addArgument(argument::Name name, argument::Value value);

    // Factory Method of the responses.
    virtual ResponseMessage parseResponse (Json::Value const & value) = 0;

public:
    Method & execute ( Connection & connection ); // throw (ConnectionException);
    const ResponseMessage & getResponse();
    virtual ~Method() = default;

};

}

#endif // COMMUNICATION_METHOD_H
// kate: indent-mode cstyle; replace-tabs on; 

