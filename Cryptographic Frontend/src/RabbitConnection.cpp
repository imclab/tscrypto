#include <amqp.h>
#include <amqp_framing.h>
#include <exception>
#include <sstream>
#include "RabbitConnection.h"


using namespace cf;

// RAII :)
RabbitConnection::~RabbitConnection() {
  amqp_bytes_free(replyToQueue);
  amqp_channel_close(connection, channel, AMQP_REPLY_SUCCESS);
  amqp_connection_close(connection, AMQP_REPLY_SUCCESS);
}

void RabbitConnection::send(std::string message) const {
  amqp_basic_properties_t props;
  props._flags =
      AMQP_BASIC_CONTENT_TYPE_FLAG |
      AMQP_BASIC_DELIVERY_MODE_FLAG |
      AMQP_BASIC_REPLY_TO_FLAG |
      AMQP_BASIC_CORRELATION_ID_FLAG;
  props.content_type = amqp_cstring_bytes("text/plain");
  props.delivery_mode = 2; // Persistente
  props.reply_to = amqp_bytes_malloc_dup(replyToQueue);

  if (props.reply_to.bytes == nullptr)
    throw std::bad_alloc();

  props.correlation_id = amqp_cstring_bytes("1"); // Generar UUID para esto

  amqp_basic_publish(connection,
                     1,
                     amqp_cstring_bytes(exchange.c_str()),
                     amqp_cstring_bytes(routingKey.c_str()),
                     0, 0, &props,
                     amqp_cstring_bytes(message.c_str()));
}


std::string RabbitConnection::receive() const {
  std::stringstream ss;

  amqp_basic_consume(connection, channel, replyToQueue, amqp_empty_bytes, 0, 1, 0, amqp_empty_table);
  amqp_frame_t frame;
  int result;

  amqp_basic_deliver_t *d;
  size_t bodyTarget;
  size_t bodyReceived;

  while (true) {
    amqp_maybe_release_buffers(connection);
    result = amqp_simple_wait_frame(connection, &frame);
    if (result < 0)
      break;
    if (frame.frame_type != AMQP_FRAME_METHOD)
      continue;

    if (frame.payload.method.id != AMQP_BASIC_DELIVER_METHOD)
      continue;

    d = static_cast<amqp_basic_deliver_t*>(frame.payload.method.decoded);
    result = amqp_simple_wait_frame(connection, &frame);
    if (result < 0)
      break;

    // Variables de control de recepcion del mensaje.
    bodyTarget = frame.payload.properties.body_size;
    bodyReceived = 0;

    while (bodyReceived < bodyTarget) {
      result = amqp_simple_wait_frame(connection, &frame);
      if (result < 0)
        break;

      if (frame.frame_type != AMQP_FRAME_BODY)
        throw BadResponseException();

      bodyReceived += frame.payload.body_fragment.len;

      ss << std::string(static_cast<char const*>(frame.payload.body_fragment.bytes), frame.payload.body_fragment.len);
    }

    break;
  }

  return ss.str();
}

RabbitConnection::RabbitConnection(std::string host, 
                                   int port,
                                   std::string exchange_,
                                   std::string routingKey_,
                                   amqp_channel_t channel_)
{
  connection = amqp_new_connection();
  sockFd = amqp_open_socket(host.c_str(), port);

  if (sockFd < 0) // Actualizo el manejo de errores a la antigua :P.
    throw CannotConnectException();

  amqp_set_sockfd(connection, sockFd);
  amqp_login(connection, "/", 0, 131072, 0, AMQP_SASL_METHOD_PLAIN, "guest", "guest");

  channel = channel_;
  amqp_channel_open(connection, channel);

  amqp_queue_declare_ok_t* r =
      amqp_queue_declare(connection, channel, amqp_empty_bytes, 0, 0, 0, 1, amqp_empty_table);

  replyToQueue = amqp_bytes_malloc_dup(r->queue);
  if (replyToQueue.bytes == nullptr)
    throw std::bad_alloc();


  exchange = exchange_;
  routingKey = routingKey_;
  
}

