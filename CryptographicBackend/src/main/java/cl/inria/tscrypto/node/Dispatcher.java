package cl.inria.tscrypto.node;

import java.io.Closeable;
import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import cl.inria.tscrypto.common.messages.TSMessage;

public class Dispatcher implements Closeable {

	private Channel channel;

	public Dispatcher(Connection connection) throws IOException {
		channel = connection.createChannel();
	}

	public void send(TSMessage message, String replyTo) throws IOException {

		channel.basicPublish(
    			"",			// exchange
    			replyTo,	// queue name
    			null,		// other properties for the message - routing headers etc
    			message.toJsonPretty().getBytes()
    			);
	}

    @Override
    public void close() throws IOException {
        channel.close();
    }
}
