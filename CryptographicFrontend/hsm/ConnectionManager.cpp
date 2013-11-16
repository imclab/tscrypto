/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#include <communication/RabbitConnection.h>
#include "ConnectionManager.h"
#include "Configuration.h"

using namespace hsm;
using namespace communication;

ConnectionManager::ConnectionManager ( const Configuration& configuration )
{
    Configuration::RabbitMqConf const & rabbitmqConf = configuration.getRabbitMqConf();
    hostname_ = rabbitmqConf.hostname;
    port_ = std::stoi ( rabbitmqConf.port );
    rpcQueue_ = rabbitmqConf.rpcQueue;
}

Connection* ConnectionManager::getConnection() const
{
    // NOTE: this simple implementation creates a new connection for each need of one...
    return new RabbitConnection ( hostname_, port_, "", rpcQueue_, 1 );
}
