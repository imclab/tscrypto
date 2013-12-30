package cl.inria.tscrypto.sigDealer;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.inria.tscrypto.common.config.RabbitMQConfig;
import cl.inria.tscrypto.common.messages.TSMessage;
import cl.inria.tscrypto.common.utils.TSLogger;
import cl.inria.tscrypto.sigDealer.handlers.SDHandler;
import cl.inria.tscrypto.sigDealer.handlers.HandlerFactory;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class ResultsCollector extends DefaultConsumer {

	private ExecutorService executor;
	private RequestManager manager;

	public ResultsCollector(Connection connection, RequestManager requestManager) throws IOException {
        super(connection.createChannel());

        SDConfig config = SDConfig.getInstance();

        RabbitMQConfig rconfig = config.getRabbitMQConfig();

        executor = Executors.newScheduledThreadPool(config.getThreadsResults());
        // TODO Check if the manager is thread-safe
        manager = requestManager;

        RabbitMQDeclare.declareQueue(getChannel(), rconfig);

        boolean autoAck = true;
        getChannel().basicConsume(
                rconfig.getClientQueue(),
                autoAck,
                this
        );

	}

	public void handleShutdownSignal(Object object, Object object2) {
        try {
            getChannel().close();
            getChannel().getConnection().close();
            executor.shutdown();
        } catch (IOException e) {
            TSLogger.node.error("Error when shutting down Results Collector " + e.getMessage(), e);
        }
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
            BasicProperties properties, byte[] body) throws IOException {

        executor.execute(new Delivery(body));
    }
    
    public class Delivery implements Runnable {
        byte[] body;

        Delivery(byte[] body) {
            this.body = body;
        }

        @Override
        public void run() {
            try {
                handleMessage(body);
            } catch (ParseException e) {
                TSLogger.node.error("Parsing Error for msg: "
                        + new String(body), e);
            } catch (Exception e) {
                TSLogger.node.error("Unknown Exception for msg: "
                        + new String(body), e);
            }
        }

        private void handleMessage(byte[] body)
                throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException {
            String jsonText = new String(body);
            TSMessage message = TSMessage.fromJson(jsonText);

            SDHandler handler = HandlerFactory.createMessageHandler(message);
			handler.handle(manager);
        }
    }

}
