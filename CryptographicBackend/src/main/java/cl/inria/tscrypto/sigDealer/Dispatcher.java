package cl.inria.tscrypto.sigDealer;

import java.io.Closeable;
import java.io.IOException;

import cl.inria.tscrypto.common.config.RabbitMQConfig;
import cl.inria.tscrypto.common.messages.TSMessage;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Dispatcher implements Closeable {

	private Channel channel;
	private RabbitMQConfig rconfig;
	

	public Dispatcher(Connection connection, RabbitMQConfig rconfig) throws IOException {
		this.rconfig = rconfig;
		channel = connection.createChannel();
	}
	
	public void send(String alias, TSMessage message) throws IOException {

		channel.basicPublish(
				rconfig.getRequestsExchange(alias),
    			"", 	// routing key
    			null,	// other properties for the message - routing headers etc
    			message.toJsonPretty().getBytes()
    			);

	}

    @Override
    public void close() throws IOException {
        channel.close();
    }
}
