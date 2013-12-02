/**
 * @file
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef COMMUNICATION_RABBITCONNECTION_H
#define COMMUNICATION_RABBITCONNECTION_H
#include "Connection.h"
#include <amqp.h>

namespace communication
{

class RabbitConnection : public Connection
{
private:
    int sockFd_;
    amqp_connection_state_t connection_;
    amqp_channel_t channel_;
    amqp_bytes_t replyToQueue_;
    std::string exchange_;
    std::string routingKey_;

protected:
    RabbitConnection ( RabbitConnection const & rhs ) = delete;    
    RabbitConnection & operator= ( RabbitConnection const & rhs ) = delete;
    virtual void send ( const std::string & message ) const;
    virtual std::string receive() const;

public:
    RabbitConnection();
    RabbitConnection ( const std::string & host, int port, const std::string & exchange,
                       const std::string & routingKey, amqp_channel_t channel );
    RabbitConnection ( RabbitConnection && other );
    RabbitConnection& operator= ( RabbitConnection && rhs );
    virtual ~RabbitConnection();

};

}

#endif // RabbitConnection_H_
// kate: indent-mode cstyle; replace-tabs on; 
