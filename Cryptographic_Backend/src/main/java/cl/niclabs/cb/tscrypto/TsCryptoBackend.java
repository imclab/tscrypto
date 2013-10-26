package cl.niclabs.cb.tscrypto;

import cl.inria.tscrypto.common.utils.TSConnection;
import cl.inria.tscrypto.sigDealer.SDConfig;

import cl.niclabs.cb.backend.methods.MethodFactory;
import cl.niclabs.cb.dispatcher.MethodDispatcher;
import cl.niclabs.cb.tscrypto.methods.TsCryptoMethodFactory;
import com.rabbitmq.client.*;

import java.io.IOException;

public class TsCryptoBackend {
    private static void run(SDConfig config)
            throws ShutdownSignalException, ConsumerCancelledException, InterruptedException, IOException {
        String queueName = config.getRpcQueue();
        Connection connection = TSConnection.getConnection(config.getRabbitMQConfig());

        MethodFactory methodFactory = new TsCryptoMethodFactory(connection, config);
        Channel channel;
        QueueingConsumer consumer;

        try {
            channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);
            channel.basicQos(1);
            consumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, false, consumer);
        }
        catch (IOException e) {
            System.err.println("No se puede conectar al servidor...");
            System.err.println(e.getLocalizedMessage());
            System.exit(1);
            return; // Mañas de java :).
        }

        System.err.println("Esperando solicitudes...");

        //noinspection InfiniteLoopStatement
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            AMQP.BasicProperties props = delivery.getProperties();
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties()
                    .builder()
                    .correlationId(props.getCorrelationId())
                    .build();

            String message = new String(delivery.getBody());
            System.err.println(message);

            String response = MethodDispatcher.dispatch(message, methodFactory);

            try {
                System.err.println("Enviando " + response + "...");
                channel.basicPublish("", props.getReplyTo(), replyProps, response.getBytes());
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
            catch (Exception e) {
                System.err.println("No se pudo enviar mensaje al servidor...");
                System.err.println(e.getLocalizedMessage());
            }
        }
    }

    public static void main(String[] args)
            throws ShutdownSignalException, ConsumerCancelledException, InterruptedException, IOException {
        run(new SDConfig());
    }
}
