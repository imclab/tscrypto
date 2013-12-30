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

#ifndef COMMUNICATION_METHODMESSAGE_H
#define COMMUNICATION_METHODMESSAGE_H

#include <string>
#include "Argument.h"

namespace communication
{

class MethodMessage
{
private:
    std::string name_;
    argument::Map argMap_;

public:
    MethodMessage ( const std::string & name );
    MethodMessage (MethodMessage && message) = default;
    MethodMessage (const MethodMessage & message) = default;

    virtual ~MethodMessage() = default;
    virtual void addArgument(argument::Name name, argument::Value value);
    virtual std::string toJson();
};


}

#endif
// kate: indent-mode cstyle; replace-tabs on; 
