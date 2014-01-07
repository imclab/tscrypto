/*
    Copyright 2013 NIC Chile Research Labs
    This file is part of TsCrypto.

    TsCrypto is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TsCrypto is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TsCrypto.  If not, see <http://www.gnu.org/licenses/>.
 */

package cl.niclabs.tscrypto.common.utils;

import java.io.IOException;

import cl.niclabs.tscrypto.common.config.RabbitMQConfig;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class TSConnection {
    private static Connection getConnection(String username, String password,
            String host, int port) throws IOException {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUsername(username);
            factory.setPassword(password);
            factory.setHost(host);
            factory.setPort(port);

            return factory.newConnection();
        }
        
        public static Connection getConnection(RabbitMQConfig config) throws IOException{
            TSLogger.node.debug("New RabbitMQ Connection:"+config);

            return getConnection(
                    config.getUsername(),
                    config.getPassword(),
                    config.getHost(),
                    config.getPort());
        }


}
