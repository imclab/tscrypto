package cl.inria.tscrypto.common.utils;

import java.io.IOException;

import cl.inria.tscrypto.common.config.RabbitMQConfig;

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
