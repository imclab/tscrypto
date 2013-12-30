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

#ifndef COMMUNICATION_SEEDRANDOMMETHOD_HPP
#define COMMUNICATION_SEEDRANDOMMETHOD_HPP
#include "Method.h"

namespace communication
{

class SeedRandomMethod : public Method
{
public:
    SeedRandomMethod ( std::string sessionHandler, std::string seed );
    virtual ~SeedRandomMethod() = default;
    virtual ResponseMessage parseResponse ( const Json::Value& value ) override;
};

}

#endif // SEEDRANDOMMETHOD_HPP
// kate: indent-mode cstyle; replace-tabs on; 
