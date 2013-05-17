/** 
 * @file 
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */ 

#ifndef RabbitConnection_H_
#define RabbitConnection_H_
#include "Connection.h"
#include <amqp.h>

namespace cf {

class RabbitConnection : public Connection { 
  private:
    int sockFd;
    amqp_connection_state_t connection;
    amqp_channel_t channel;
    amqp_bytes_t replyToQueue;
    std::string exchange;
    std::string routingKey;

    RabbitConnection& operator=(RabbitConnection const& rhs);
  protected:
    RabbitConnection();
    RabbitConnection(RabbitConnection const& rhs);
    virtual void send(std::string message) const;
    virtual std::string receive() const;
  public: 
    RabbitConnection(std::string host, int port, std::string exchange, std::string routingKey, amqp_channel_t channel);
    virtual ~RabbitConnection();
}; 

} 

#endif // RabbitConnection_H_
