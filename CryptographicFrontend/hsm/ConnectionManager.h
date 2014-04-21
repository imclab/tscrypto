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

#ifndef HSM_CONNECTIONMANAGER_H
#define HSM_CONNECTIONMANAGER_H

#include <Connection.h>
using namespace communication;

namespace hsm
{
struct IConnectionManager {
    virtual ~IConnectionManager() = default;
    virtual IConnection & getConnection() = 0;
};
    
    
template<typename ConnectionImpl>
class ConnectionManager : public IConnectionManager
{   
    Connection<ConnectionImpl> conn_;
public:
    template<typename... Args>
    ConnectionManager(Args... args) : conn_(args...) {}
    
    ~ConnectionManager() = default;
    IConnection & getConnection() override {
	return conn_;
    }
};

}

#endif // HSM_CONNECTIONMANAGER_H
