/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef HSM_CONNECTIONMANAGER_H
#define HSM_CONNECTIONMANAGER_H

namespace communication {
  class Connection;
}
using communication::Connection;

namespace hsm
{

class Configuration;

class ConnectionManager
{  
public:
    virtual ~ConnectionManager() = default;
    virtual const Connection & getConnection() const = 0;
};

}

#endif // HSM_CONNECTIONMANAGER_H
