#include <iostream>
#include <amqp.h>
#include <amqp_framing.h>
#include <exception>
#include <sstream>

#include <boost/uuid/uuid.hpp>
#include <boost/uuid/uuid_generators.hpp>
#include <boost/uuid/uuid_io.hpp>

#include "RabbitConnection.h"

using namespace communication;

RabbitConnection::RabbitConnection()
    : channel_(-1), sockFd_(-1), connection_(nullptr),
    replyToQueue_( {0, nullptr})
{

}


RabbitConnection::RabbitConnection ( const std::string & host,
                                     int port,
                                     const std::string & exchange,
                                     const std::string & routingKey,
                                     amqp_channel_t channel )
    : channel_ ( channel ), exchange_ ( exchange ), routingKey_ ( routingKey )
{
    sockFd_ = amqp_open_socket ( host.c_str(), port );

    if ( sockFd_ < 0 ) { // Actualizo el manejo de errores a la antigua :P.
        throw CannotConnectException();
    }

    connection_ = amqp_new_connection();
    amqp_set_sockfd ( connection_, sockFd_ );
    amqp_login ( connection_, "/", 0, 131072, 0, AMQP_SASL_METHOD_PLAIN, "guest", "guest" );

    amqp_channel_open ( connection_, channel_ );

    amqp_queue_declare_ok_t * r =
        amqp_queue_declare ( connection_, channel_, amqp_empty_bytes, 0, 0, 0, 1, amqp_empty_table );

    replyToQueue_ = amqp_bytes_malloc_dup ( r->queue );
    if ( replyToQueue_.bytes == nullptr ) {
        throw std::bad_alloc();
    }

}

RabbitConnection::RabbitConnection(RabbitConnection&& other)
    : sockFd_(other.sockFd_), connection_(other.connection_),
      channel_(other.channel_), replyToQueue_(other.replyToQueue_),
      exchange_(std::move(other.exchange_)), routingKey_(std::move(other.routingKey_))
{
    // Invalidate the other connection...
    other.sockFd_ = -1;
    other.connection_ = nullptr;
    other.channel_ = -1;
    other.replyToQueue_.bytes = nullptr;
    other.replyToQueue_.len = 0;
    other.exchange_.clear();
    other.routingKey_.clear();
}

RabbitConnection& RabbitConnection::operator=(RabbitConnection&& rhs)
{
    if (connection_ != nullptr) {        
        // first, destruct this connection...
        this->~RabbitConnection();
    } 

    sockFd_ = rhs.sockFd_;
    rhs.sockFd_ = -1;

    connection_ = rhs.connection_;
    rhs.connection_ = nullptr;

    channel_ = rhs.channel_;
    rhs.channel_ = -1;

    replyToQueue_ = rhs.replyToQueue_;
    rhs.replyToQueue_.bytes = nullptr;
    rhs.replyToQueue_.len = 0;

    exchange_ = std::move(rhs.exchange_);
    routingKey_ = std::move(rhs.routingKey_);

}


// RAII :)
RabbitConnection::~RabbitConnection()
{
    if (replyToQueue_.bytes != nullptr) { // Connection not inited...
        amqp_bytes_free ( replyToQueue_ );
        amqp_channel_close ( connection_, channel_, AMQP_REPLY_SUCCESS );
        amqp_connection_close ( connection_, AMQP_REPLY_SUCCESS );
        amqp_destroy_connection ( connection_ );
    }
}

void RabbitConnection::send ( const std::string & message ) const
{
    amqp_basic_properties_t props;
    props._flags =
        AMQP_BASIC_CONTENT_TYPE_FLAG |
        AMQP_BASIC_DELIVERY_MODE_FLAG |
        AMQP_BASIC_REPLY_TO_FLAG |
        AMQP_BASIC_CORRELATION_ID_FLAG;
    props.content_type = amqp_cstring_bytes ( "text/plain" );
    props.delivery_mode = 2; // Persistente
    props.reply_to = amqp_bytes_malloc_dup ( replyToQueue_ );

    if ( props.reply_to.bytes == nullptr ) {
        throw std::bad_alloc();
    }

    boost::uuids::uuid uuid = boost::uuids::random_generator() ();
    std::string uuidString = boost::uuids::to_string ( uuid );
    props.correlation_id = amqp_cstring_bytes ( uuidString.c_str() );

    amqp_basic_publish ( connection_,
                         1,
                         amqp_cstring_bytes ( exchange_.c_str() ),
                         amqp_cstring_bytes ( routingKey_.c_str() ),
                         0, 0, &props,
                         amqp_cstring_bytes ( message.c_str() ) );
    amqp_bytes_free ( props.reply_to );
}


std::string RabbitConnection::receive() const
{
    std::stringstream ss;

    amqp_basic_consume ( connection_, channel_, replyToQueue_, amqp_empty_bytes, 0, 1, 0, amqp_empty_table );
    amqp_frame_t frame;
    int result;

    size_t bodyTarget;
    size_t bodyReceived;

    while ( true ) {
        amqp_maybe_release_buffers ( connection_ );
        result = amqp_simple_wait_frame ( connection_, &frame );
        if ( result < 0 ) {
            break;
        }
        if ( frame.frame_type != AMQP_FRAME_METHOD ) {
            continue;
        }

        if ( frame.payload.method.id != AMQP_BASIC_DELIVER_METHOD ) {
            continue;
        }

        result = amqp_simple_wait_frame ( connection_, &frame );
        if ( result < 0 ) {
            break;
        }

        // Variables de control de recepcion del mensaje.
        bodyTarget = frame.payload.properties.body_size;
        bodyReceived = 0;

        while ( bodyReceived < bodyTarget ) {
            result = amqp_simple_wait_frame ( connection_, &frame );
            if ( result < 0 ) {
                break;
            }

            if ( frame.frame_type != AMQP_FRAME_BODY ) {
                throw BadResponseException();
            }

            bodyReceived += frame.payload.body_fragment.len;

            ss << std::string ( static_cast<char const *> ( frame.payload.body_fragment.bytes ), frame.payload.body_fragment.len );
        }

        break;
    }

    return ss.str();
}


// kate: indent-mode cstyle; replace-tabs on; 
