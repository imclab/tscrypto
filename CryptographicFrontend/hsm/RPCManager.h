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

#include <RPC.h>
#include <memory>
using namespace communication;

namespace hsm
{
struct AbstractRPCManager {
    virtual ~AbstractRPCManager() = default;
    virtual AbstractRPC & getRPC() = 0;
};
    
    
template<typename Connection>
class RPCManager : public AbstractRPCManager
{   
    using RPCPtr = std::unique_ptr<RPC<Connection>>;
    RPCPtr rpc_;
public:
    template<typename... Args>
    RPCManager(Args... args) : rpc_(new RPC<Connection>(args...)) {}
    RPCManager() = default;
    RPCManager(RPCManager &&) = default;
    RPCManager & operator=(RPCManager &&) = default;
    
    
    ~RPCManager() = default;
    AbstractRPC & getRPC() override;
private:
    RPCManager(RPCManager &) = delete;
    RPCManager & operator=(RPCManager &) = delete;
};

template<typename Connection>
AbstractRPC& RPCManager<Connection>::getRPC()
{
    return *rpc_;
}


}

#endif // HSM_CONNECTIONMANAGER_H
