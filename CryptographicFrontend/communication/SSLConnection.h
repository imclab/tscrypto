#ifndef COMMUNICATION_SSLCONNECTION_H
#define COMMUNICATION_SSLCONNECTION_H


#include <Connection.h>

namespace communication {
    class SSLConnection : public Connection {
        int socket;
        SSL
    protected:
        virtual void send( const std::string & message ) const;
        virtual void std::string receive() const;
    public:
        SSLConnection (SSLConnection const & rhs) = delete;
        SSLConnection & operator= (SSLConnection const & rhs) = delete;
        SSLConnection ();
        SSLConnection (SSLConnection && other);
        SSLConnection & operator= (SSLConnection && other);
        virtual ~SSLConnection();
        
    };
}

#endif
