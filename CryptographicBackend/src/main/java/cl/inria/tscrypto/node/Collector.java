package cl.inria.tscrypto.node;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.inria.tscrypto.common.config.RabbitMQConfig;
import cl.inria.tscrypto.common.messages.MessageAsync;
import cl.inria.tscrypto.common.messages.TSMessage;
import cl.inria.tscrypto.common.utils.TSLogger;
import cl.inria.tscrypto.node.handlers.NodeHandler;
import cl.inria.tscrypto.node.handlers.HandlerFactory;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;

public class Collector extends DefaultConsumer {
	
    private ExecutorService executor;
    private Signer signer;
    private Dispatcher dispatcher;

    public Collector(Connection connection, Dispatcher dispatcher, KeyShareManager keyShareManager) throws IOException {
        super(connection.createChannel());
        NodeConfig config = NodeConfig.getInstance();
        RabbitMQConfig rconfig = config.getRabbitMQConfig();

        this.signer = new Signer(keyShareManager);
        this.dispatcher = dispatcher;
        this.executor = Executors.newScheduledThreadPool(config.getNumThreads());

        String alias = config.getRabbitMQConfig().getSignRequestAlias();
        RabbitMQDeclare.declareExchanges(getChannel(), rconfig, alias);
        RabbitMQDeclare.declareAndBindQueues(getChannel(), rconfig, config.getNodeId(), alias);

        boolean autoAck = true;
        getChannel().basicConsume(
                rconfig.getRequestsQueue(config.getNodeId()),
                autoAck,
                this
        );

    }



    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
        try {
            getChannel().close();
            getChannel().getConnection().close();
            executor.shutdown();
        } catch (IOException e) {
            TSLogger.node.error("Error when shuttingdown Collector " + e.getMessage(), e);
        }
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
            BasicProperties properties, byte[] body) throws IOException {

        executor.execute(new Delivery(body));
    }
    
    private class Delivery implements Runnable {

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
        
        private void handleMessage(byte[] body) throws ParseException, ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
            String jsonText = new String(body);
            TSMessage message = TSMessage.fromJson(jsonText);
            
			NodeHandler handler = HandlerFactory.createMessageHandler(message);

			MessageAsync response = handler.handle(signer);
			if (response != null) {
				dispatcher.send(response, response.getReplyTo());
			}
        }
    }
}
