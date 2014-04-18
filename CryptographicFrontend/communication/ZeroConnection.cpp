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

#include "ZeroConnection.h"

std::string communication::ZeroConnection::receive()
{
    zmq::message_t reply;
    socket_.recv(&reply);   
    
    char * data = reinterpret_cast<char*>(reply.data());
    std::size_t length = reply.size();
    std::string response(data, length);
    return response;
}

void communication::ZeroConnection::send(const std::string& message)
{
    std::size_t length = message.size();
    zmq::message_t zmqMessage (length);

    std::memcpy(zmqMessage.data(), message.data(), length);
    
    socket_.send(zmqMessage);
}

communication::ZeroConnection::ZeroConnection(std::string host, std::string port)
: context_(1) , socket_(context_, ZMQ_REQ)
{
    socket_.connect(("tcp://" + host + ":" + port).data());;    
}

