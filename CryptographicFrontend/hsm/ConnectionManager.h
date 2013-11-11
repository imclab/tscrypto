/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef HSM_CONNECTIONMANAGER_H
#define HSM_CONNECTIONMANAGER_H

#include <memory>

namespace communication {
class Connection;
}

namespace hsm {

class Configuration;

class ConnectionManager
{
public:
  ConnectionManager(Configuration const & configuration);
  virtual ~ConnectionManager() = default;
  communication::Connection * getConnection() const;
private:
    std::string hostname_;
    int port_;
    std::string rpcQueue_;
};
using ConnectionPtr = std::unique_ptr<communication::Connection>;

}

#endif // HSM_CONNECTIONMANAGER_H
