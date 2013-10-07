package cl.niclabs.cb.dispatcher;

import java.io.IOException;

import cl.niclabs.cb.backend.methods.MethodFactory;
import cl.niclabs.cb.backend.methods.implementation.SimpleSignMethodFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class Backend {

    private static void run(String queueName, String hostName, MethodFactory methodFactory)
            throws ShutdownSignalException, ConsumerCancelledException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(hostName);
        Connection connection;
        Channel channel;
        QueueingConsumer consumer;

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);
            channel.basicQos(1);
            consumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, false, consumer);
        }
        catch (IOException e) {
            System.out.println("No se puede conectar al servidor...");
            System.out.println(e.getLocalizedMessage());
            System.exit(1);
            return;
        }

        System.out.println("Esperando solicitudes...");

        //noinspection InfiniteLoopStatement
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            BasicProperties props = delivery.getProperties();
            BasicProperties replyProps = new BasicProperties()
                    .builder()
                    .correlationId(props.getCorrelationId())
                    .build();

            String message = new String(delivery.getBody());
            System.out.println(message);

            String response = MethodDispatcher.dispatch(message, methodFactory);
            try {
                System.out.println("Enviando " + response + "...");
                channel.basicPublish("", props.getReplyTo(), replyProps, response.getBytes());
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
            catch (Exception e) {
                System.out.println("No se pudo enviar mensaje al servidor...");
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

	public static void main(String[] args) 
			throws ShutdownSignalException, ConsumerCancelledException, InterruptedException {
        String queueName = "";
        String hostName = "";

        // Manejo de argumentos...
        switch (args.length) {
            case 0:
                hostName = "localhost";
                queueName = "rpc_queue";
                break;

            case 1:
                hostName = args[0];
                queueName = "rpc_queue";
                break;

            case 2:
                hostName = args[0];
                queueName = args[1];
                break;

            default:
                System.err.println("Muchos argumentos.");
                System.exit(1);
        }

        run(queueName, hostName, SimpleSignMethodFactory.getInstance());
	}

}
