/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef HSM_RABBITCONNECTIONMANAGER_H
#define HSM_RABBITCONNECTIONMANAGER_H

#include <RabbitConnection.h>
#include <Configuration.h>
#include <ConnectionManager.h>

namespace hsm {

class RabbitConnectionManager : public ConnectionManager
{ 
    std::string hostname_;
    int port_;
    std::string rpcQueue_;
    communication::RabbitConnection connection_;
    
public:        
    RabbitConnectionManager() = default;
    RabbitConnectionManager (const Configuration::RabbitMqConf& configuration);
    RabbitConnectionManager ( RabbitConnectionManager &) = delete;
    RabbitConnectionManager ( RabbitConnectionManager && other ) = default;
    
    RabbitConnectionManager & operator=(RabbitConnectionManager &) = delete;
    RabbitConnectionManager & operator=(RabbitConnectionManager && rhs) = default;
    
    virtual ~RabbitConnectionManager() = default;
    
    virtual void init( const Configuration::RabbitMqConf & configuration );
    virtual const Connection& getConnection() const;
};
}

#endif // HSM_RABBITCONNECTIONMANAGER_H
