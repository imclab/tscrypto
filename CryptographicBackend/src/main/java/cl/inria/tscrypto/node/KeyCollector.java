package cl.inria.tscrypto.node;

import cl.inria.tscrypto.common.messages.SendKeyAnswer;
import cl.inria.tscrypto.common.messages.SendKeyQuery;
import cl.inria.tscrypto.common.messages.TSMessage;
import cl.inria.tscrypto.common.utils.TSLogger;
import cl.inria.tscrypto.common.utils.Util;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KeyCollector extends DefaultConsumer {

    private ExecutorService executor;
    private KeyShareManager manager;
    private Dispatcher dispatcher;

    public KeyCollector(Connection connection, Dispatcher dispatcher, KeyShareManager manager) throws IOException {
        super(connection.createChannel());

        NodeConfig config = NodeConfig.getInstance();
        this.dispatcher = dispatcher;
        this.manager = manager;
        this.executor = Executors.newScheduledThreadPool(config.getNumThreads());

        // The name of the queue has the id...
        String queue = Util.keyQueueName(config.getRabbitMQConfig(), config.getNodeId());
        boolean autoAck = true;
        Consumer consumer = this;

        getChannel().queueDeclare(queue, false, false, false, null);
        getChannel().basicConsume(queue, autoAck, consumer);

    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        TSLogger.node.info("KeyCollector: new SendKeyQuery received.");
        TSLogger.node.debug(new String(body));
        executor.execute(new Handler(body));
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
        try {
            getChannel().close();
            getChannel().getConnection().close();
            executor.shutdown();
        } catch (IOException e) {
            TSLogger.node.error("Error when shuttingdown KeyCollector " + e.getMessage(), e);
        }
    }

    private class Handler implements Runnable {
        byte[] body;

        private Handler(byte[] body) {
            this.body = body;
        }

        @Override
        public void run() {
            NodeConfig config = NodeConfig.getInstance();

            String jsonText = new String(body);
            SendKeyQuery message = TSMessage.fromJson(jsonText);
            manager.addKey(message.getLabel(), message.getKeyInfo());
            TSLogger.node.info("Key " + message.getLabel() + " added to manager.");

            SendKeyAnswer response = new SendKeyAnswer(
                    message.ticket,
                    config.getNodeId(),
                    message.getReplyTo()
            );

            try {
                dispatcher.send(response, message.getReplyTo());
            } catch (IOException e) {
                TSLogger.node.error("Cannot send ACK", e);
            }
        }
    }


}
