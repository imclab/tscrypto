package cl.inria.tscrypto.node;

import java.io.IOException;

import cl.inria.tscrypto.common.config.RabbitMQConfig;

import com.rabbitmq.client.Channel;

public class RabbitMQDeclare {

	public static void declareAndBindQueues(Channel channel, RabbitMQConfig rconfig, int id, String keyAlias) throws IOException {
		channel.queueDeclare(
				rconfig.getRequestsQueue(id),			// queue name
				rconfig.getQueueDeclareDurable(),
				rconfig.getQueueDeclareExclusive(),
				rconfig.getQueueDeclareAutodelete(),
				null	// arguments
				);
		
		channel.queueBind(
				rconfig.getRequestsQueue(id),	// queue name
				rconfig.getRequestsExchange(keyAlias),
				""	// routing key
				);
	}

	public static void declareExchanges(Channel channel, RabbitMQConfig rconfig, String keyAlias) throws IOException {
		channel.exchangeDeclare(
				rconfig.getRequestsExchange(keyAlias),	// exchange
				"fanout",	// type
				rconfig.getExchangeDeclareDurable(),
				rconfig.getExchangeDeclareAutodelete(),
				rconfig.getExchangeDeclareInternal(),
				null	// arguments
				);

	}

}
