/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef HSM_CONNECTIONMANAGER_H
#define HSM_CONNECTIONMANAGER_H

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
  communication::Connection * createConnection() const;
private:
    std::string hostname_;
    int port_;
    std::string rpcQueue_;
};
}

#endif // HSM_CONNECTIONMANAGER_H
