package cl.inria.tscrypto.sigDealer;

import java.io.IOException;

import cl.inria.tscrypto.common.config.RabbitMQConfig;

import com.rabbitmq.client.Channel;

public class RabbitMQDeclare {

	public static void declareQueue(Channel channel, RabbitMQConfig rconfig) throws IOException {
		channel.queueDeclare(
				rconfig.getClientQueue(),
				rconfig.getQueueDeclareDurable(),
				rconfig.getQueueDeclareExclusive(),
				rconfig.getQueueDeclareAutodelete(),
				null	// arguments
				);
	}

    public static void declareKeyManagementQueue(Channel channel, RabbitMQConfig rconfig) throws IOException {
        channel.queueDeclare(
                rconfig.getKeyManagementQueue(),
                rconfig.getQueueDeclareDurable(),
                rconfig.getQueueDeclareExclusive(),
                rconfig.getQueueDeclareAutodelete(),
                null	// arguments
        );
    }

}
