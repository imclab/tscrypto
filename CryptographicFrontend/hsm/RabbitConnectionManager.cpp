/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "Configuration.h"
#include "RabbitConnectionManager.h"

using namespace hsm;
using namespace communication;

RabbitConnectionManager::RabbitConnectionManager(const Configuration::RabbitMqConf& c)
    : connection_(c.hostname,std::stoi( c.port ), "", c.rpcQueue, 1)
{
    hostname_ = c.hostname;
    port_ = std::stoi ( c.port );
    rpcQueue_ = c.rpcQueue;
}

void RabbitConnectionManager::init(const Configuration::RabbitMqConf& c)
{
    // Move semantics... (A connection handler cannot be copied...)
    connection_ = RabbitConnection(c.hostname,std::stoi( c.port ), "", c.rpcQueue, 1);
    hostname_ = c.hostname;
    port_ = std::stoi ( c.port );
    rpcQueue_ = c.rpcQueue;
}


const Connection& RabbitConnectionManager::getConnection() const
{
    return connection_;
}
