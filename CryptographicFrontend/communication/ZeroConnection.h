/*
 * <one line to give the library's name and an idea of what it does.>
 * Copyright (C) 2014  Francisco Cifuentes <email>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

#ifndef ZEROCONNECTION_H
#define ZEROCONNECTION_H
#include <zmq.hpp>

namespace communication {
class ZeroConnection
{    
private:
    zmq::context_t context_;
    zmq::socket_t socket_;
public:
    ZeroConnection(std::string host, std::string port);
    ZeroConnection() = default;
    ZeroConnection(ZeroConnection && other);
    ZeroConnection & operator=(ZeroConnection && rhs);
    
    ZeroConnection & operator=(ZeroConnection &) = delete;
    ZeroConnection(ZeroConnection & other) = delete;
    std::string receive();
    void send(const std::string& message);
    ~ZeroConnection() = default;
};

}

#endif // ZEROCONNECTION_H
