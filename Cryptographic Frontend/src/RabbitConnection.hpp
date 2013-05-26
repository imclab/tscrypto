/**
 * @file
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef RabbitConnection_H_
#define RabbitConnection_H_
#include <Connection.hpp>
#include <amqp.h>

namespace cf
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

    RabbitConnection &operator=(RabbitConnection const &rhs);
protected:
    RabbitConnection();
    RabbitConnection(RabbitConnection const &rhs);
    virtual void send(std::string message) const;
    virtual std::string receive() const;
public:
    RabbitConnection(std::string host, int port, std::string exchange, std::string routingKey, amqp_channel_t channel);
    virtual ~RabbitConnection();
};

}

#endif // RabbitConnection_H_
