/**
 * @file
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#ifndef RabbitConnection_H_
#define RabbitConnection_H_
#include "Connection.hpp"
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

  RabbitConnection & operator=(RabbitConnection const & rhs);
protected:
  RabbitConnection() = delete;
  RabbitConnection(RabbitConnection const & rhs) = delete;
  RabbitConnection& operator=(RabbitConnection && rhs) = delete;
  virtual void send(const std::string & message) const;
  virtual std::string receive() const;

public:
  RabbitConnection(const std::string & host, int port, const std::string & exchange,
                   const std::string & routingKey, amqp_channel_t channel);
  virtual ~RabbitConnection();
  
};

}

#endif // RabbitConnection_H_
// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
