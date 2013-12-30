package cl.inria.tscrypto.keyFactory;

import cl.inria.tscrypto.common.config.RabbitMQConfig;
import cl.inria.tscrypto.common.messages.SendKeyAnswer;
import cl.inria.tscrypto.common.messages.TSMessage;
import cl.inria.tscrypto.common.utils.TSLogger;
import cl.inria.tscrypto.sigDealer.RabbitMQDeclare;
import cl.inria.tscrypto.sigDealer.SDConfig;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KeyManagementCollector extends DefaultConsumer {
    private ExecutorService executor;
    private KeyDispatchRequestManager keyDispatchRequestManager;

    public KeyManagementCollector(Connection connection, KeyDispatchRequestManager keyRequestManager) throws IOException {
        super(connection.createChannel());
        SDConfig config = SDConfig.getInstance();
        this.keyDispatchRequestManager = keyRequestManager;
        this.executor = Executors.newScheduledThreadPool(config.getThreadsResults());

        RabbitMQConfig rconfig = config.getRabbitMQConfig();
        RabbitMQDeclare.declareKeyManagementQueue(getChannel(), rconfig);

        boolean autoAck = true;
        getChannel().basicConsume(rconfig.getKeyManagementQueue(), autoAck, this);
    }

    public void handleShutdownSignal(Object object, Object object2) {
        try {
            getChannel().close();
            getChannel().getConnection().close();
            executor.shutdown();
        } catch (IOException e) {
            TSLogger.node.error("Error when shuttingdown Key Management Results Collector " + e.getMessage(), e);
        }
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties properties, byte[] body) throws IOException {

        executor.execute(new Delivery(consumerTag, envelope, properties, body));
    }

    private class Delivery implements Runnable {

        String consumerTag;
        Envelope envelope;
        AMQP.BasicProperties properties;
        byte[] body;

        Delivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
            this.consumerTag = consumerTag;
            this.envelope = envelope;
            this.properties = properties;
            this.body = body;
        }

        @Override
        public void run() {
            try {
                handleMessage(properties, body);
            } catch (ParseException e) {
                TSLogger.node.error("Parsing Error for msg: "
                        + new String(body), e);
            } catch (Exception e) {
                TSLogger.node.error("Unknown Exception for msg: "
                        + new String(body), e);
            }
        }

        private void handleMessage(AMQP.BasicProperties properties, byte[] body)
                throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException {
            String jsonText = new String(body);
            SendKeyAnswer message = TSMessage.fromJson(jsonText);
            KeyDispatchRequest request =
                    keyDispatchRequestManager.getKeyDispatchRequest(message.ticket);

            request.setReady(message.getNodeId());
            TSLogger.keyFactory.info("Node " + message.getNodeId() + " added the key to it's manager.");
        }
    }

}
