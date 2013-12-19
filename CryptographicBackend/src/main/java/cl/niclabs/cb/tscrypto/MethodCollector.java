package cl.niclabs.cb.tscrypto;

import cl.inria.tscrypto.common.utils.TSLogger;
import cl.inria.tscrypto.keyFactory.KeyDispatchRequestManager;
import cl.inria.tscrypto.keyFactory.KeyDispatcher;
import cl.inria.tscrypto.sigDealer.RequestManager;
import cl.inria.tscrypto.sigDealer.SDConfig;
import cl.niclabs.cb.backend.methods.MethodFactory;
import cl.niclabs.cb.dispatcher.MethodDispatcher;
import cl.niclabs.cb.tscrypto.methods.TsCryptoMethodFactory;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MethodCollector extends DefaultConsumer {

    private ExecutorService executor;
    private TsCryptoMethodFactory methodFactory;

    public MethodCollector(SDConfig config, Connection connection, RequestManager requestManager, KeyDispatchRequestManager keyRequestManager) throws IOException {
        super(connection.createChannel());

        methodFactory = new TsCryptoMethodFactory(config, requestManager, keyRequestManager);

        String queue = config.getRpcQueue();

        // TODO: Put this on config file
        executor = Executors.newScheduledThreadPool(1);

        getChannel().queueDeclare(queue, false, false, false, null);
        getChannel().basicQos(1);

        boolean autoAck = false;
        getChannel().basicConsume(queue, autoAck, this);
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
        try {
            getChannel().close();
            getChannel().getConnection().close();
            methodFactory.close();
            executor.shutdown();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        executor.execute(new Handle(body, envelope, properties));
    }

    private class Handle implements Runnable {

        byte[] bytes;
        Envelope envelope;
        AMQP.BasicProperties properties;
        AMQP.BasicProperties replyProperties;

        private Handle(byte[] bytes, Envelope envelope, AMQP.BasicProperties properties) {
            this.bytes = bytes;
            this.envelope = envelope;
            this.properties = properties;
            this.replyProperties = new AMQP.BasicProperties()
                    .builder()
                    .correlationId(properties.getCorrelationId())
                    .build();
        }

        @Override
        public void run() {
            String message = new String(bytes);
            TSLogger.sd.debug(message);
            String response = MethodDispatcher.dispatch(message, methodFactory);

            try {
                TSLogger.sd.debug("Sending " + response + "...");
                getChannel().basicPublish("", properties.getReplyTo(), replyProperties, response.getBytes());
                getChannel().basicAck(envelope.getDeliveryTag(), false);
            }
            catch (Exception e) {
                TSLogger.sd.error("Cannot send message to server...", e);
            }

        }
    }
}
