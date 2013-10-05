package cl.niclabs.cb.tscrypto;

import cl.inria.tscrypto.common.utils.TSConnection;
import cl.inria.tscrypto.sigDealer.SDConfig;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.MethodFactory;
import cl.niclabs.cb.dispatcher.MethodDispatcher;
import cl.niclabs.cb.dispatcher.MethodMessage;
import cl.niclabs.cb.tscrypto.methods.TsCryptoMethodFactory;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rabbitmq.client.*;

import java.io.IOException;

public class TsCryptoBackend {
    private static void run(String queueName, String hostName, MethodFactory methodFactory)
            throws ShutdownSignalException, ConsumerCancelledException, InterruptedException {

        Gson gson = new Gson();
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
            MethodMessage mmessage;
            try {
                mmessage = gson.fromJson(message, MethodMessage.class);
            }
            catch (JsonSyntaxException e) {
                ResponseMessage.ErrorMessage(e.getLocalizedMessage());
                System.err.println(e.getLocalizedMessage());
                continue;
            }
            System.err.println("Ejecutando " + mmessage.getMethod() + "...");
            MethodDispatcher dispatcher = new MethodDispatcher(mmessage, methodFactory);
            String response = dispatcher.dispatch();
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

        SDConfig config = new SDConfig();
        Connection connection = TSConnection.getConnection(config.getRabbitMQConfig());
        run(queueName, hostName, new TsCryptoMethodFactory(connection, config));
    }
}
