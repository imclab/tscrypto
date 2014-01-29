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

#ifndef TCBHSM_CONFIGURATION_H
#define TCBHSM_CONFIGURATION_H

#include <string>
#include <vector>

namespace communication
{
class Connection;
}

namespace hsm
{
class Configuration
{
public:
    struct RabbitMqConf {
        std::string hostname;
        std::string port;
        std::string rpcQueue;
    };

    struct SlotConf {
        std::string label;
    };

    struct DatabaseConf { // TODO: review this.
        std::string path;
    };
    
    Configuration() = default;
    Configuration ( std::string configurationPath );
    Configuration ( Configuration const & ) = default;
    Configuration ( Configuration && ) = default;    
    Configuration & operator=(Configuration const &) = default;
    Configuration & operator=(Configuration &&) = default;

    virtual RabbitMqConf const & getRabbitMqConf() const;
    virtual std::vector<SlotConf> const & getSlotConf() const;
    virtual DatabaseConf const & getDatabaseConf() const;
    virtual void load(std::string configurationPath);

    virtual ~Configuration() = default;
private:
    RabbitMqConf rabbitMqConf_;
    std::vector<SlotConf> slotConf_;
    DatabaseConf databaseConf_;
};
}

#endif // TCBHSM_CONFIGURATION_H
