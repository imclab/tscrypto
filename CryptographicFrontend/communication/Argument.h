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

#ifndef COMMUNICATION_ARGUMENT_H
#define COMMUNICATION_ARGUMENT_H

#include <string>
#include <unordered_map>

#include <boost/variant/variant.hpp>
#include <boost/variant/get.hpp>

namespace communication
{

namespace argument
{
using Name = std::string;
using Value = boost::variant<int, std::string>;
using Map = std::unordered_map<argument::Name, argument::Value>;
using boost::get;
}

}

#endif
// kate: indent-mode cstyle; replace-tabs on; 
