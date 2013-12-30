package cl.inria.tscrypto.common;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import cl.inria.tscrypto.common.config.RabbitMQConfig;
import cl.inria.tscrypto.node.NodeConfig;
import cl.inria.tscrypto.common.utils.TSLogger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.AMQP.BasicProperties;

public class AbstractRabbitConnectedServlet extends HttpServlet {

	private static final long serialVersionUID = 2956124198322343330L;

	// TODO should not be node (only)
    public static Logger logger = TSLogger.node;
    protected int queueTimeout;
    private NodeConfig config;
    protected Pattern adkintunAgentHeaderPattern;
    protected Pattern adkintunAgentOSPattern;
    protected Pattern patternLegacy20ClientHackConsume;
    protected Pattern patternLegacy20ClientHackPublish;

    Channel channel;

    /**
     * Configures RabbitMQ server parameters such as host, port,  username, password, etc.
     *
     * {@inheritDoc}
     */
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        try {
            //new File(servletConfig.getInitParameter("config-file"))
            init();
        } catch (Exception ex) {
            logger.fatal(ex.getMessage(), ex);
            throw new ServletException(ex);
        }
    }


    public void init() {
        this.config = NodeConfig.getInstance();

        this.queueTimeout = config.getRabbitMQConfig().getQueueDeclareTimeout();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    protected NodeConfig getConfig() {
        return config;
    }

    /**
     * Declares a RabbitMQ queue with the specified name.
     *
     * @param queueName the name of the queue to declare
     */
    protected void queueDeclare(String queueName) {

        RabbitMQConfig rconfig = config.getRabbitMQConfig();
        
        try {
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("x-message-ttl", rconfig.getQueueDeclareTimeout());
            channel = getChannel(rconfig);
            channel.queueDeclare(queueName,
                    rconfig.getQueueDeclareDurable(),
                    rconfig.getQueueDeclareExclusive(),
                    rconfig.getQueueDeclareAutodelete(),
                    args);
        } catch (Exception e) {
            logger.error(String.format(
                    "Failed to queueDeclare(%s, %s, %s, %s, {'x-mesage-ttl: {%d}'})",
                        queueName,
                        rconfig.getQueueDeclareDurable(),
                        rconfig.getQueueDeclareExclusive(),
                        rconfig.getQueueDeclareAutodelete(),
                        rconfig.getQueueDeclareTimeout()),
                    e);
        }
    }

    private Channel getChannel(RabbitMQConfig rconfig) {
		// TODO Auto-generated method stub

    	ConnectionFactory factory;
        factory = new ConnectionFactory();
        factory.setUsername(rconfig.getUsername());
        factory.setPassword(rconfig.getPassword());
        factory.setHost(rconfig.getHost());
        factory.setPort(rconfig.getPort());
    	
		try {
			channel = factory.newConnection().createChannel();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        logger.info(String.format("RabbitMQ Channel created: %s", channel.toString() ));

        return channel;
	}

	/**
     * Declares the given exchange with the specified name
     *
     * @param exchangeName the name of the exchange to declare
     */
    protected synchronized void exchangeDeclare(String exchangeName) {
        RabbitMQConfig rconfig = config.getRabbitMQConfig();

        try {
            channel.exchangeDeclare(exchangeName, "topic",
                rconfig.getExchangeDeclareDurable(),
                rconfig.getExchangeDeclareAutodelete(),
                rconfig.getExchangeDeclareInternal(),
                null);
        } catch (Exception ex) {
            logger.error(MessageFormat.format(
                    "exchangeDeclare({0},\"topic\", {1}, {2}, {3}, null)",
                    new Object[] {
                        exchangeName,
                        rconfig.getExchangeDeclareDurable(),
                        rconfig.getExchangeDeclareAutodelete(),
                        rconfig.getExchangeDeclareInternal()
                    }), ex);
        }
    }

    /**
     * Retrieves a message from the specified queue.
     *
     * @param queueName the name of the queue to query
     * @param autoAck true auto acknowledges the message.
     * @return the response retrieved from the queue or null if no response was found
     * @throws IOException
     */
    protected GetResponse basicGet(String queueName, boolean autoAck) {
        GetResponse response = null;

        try {
            response = channel.basicGet(queueName, true);
            logger.debug(MessageFormat.format("basicGet({0}, {1}) -> {2}",
                    new Object[] {
                        queueName, autoAck,
                        ((response == null) ? "(empty)"
                                           : response.getProps().getMessageId())
                    }));
        } catch (Exception ex) {
            logger.error(MessageFormat.format(
                    "Could not basicGet({0}, ...) -> {1} {2}",
                    new Object[] { queueName, autoAck, ex.getMessage() }), ex);
        }

        return response;
    }

    /**
     * Publishes a message into an exchange using a routing key and the specified
     * header properties.
     *
     * @param exchangeName the name of the exchange to write
     * @param routingKey the name of the routing key to use
     * @param props the properties to place in the header and envelope of the message
     * @param message the message in raw bytes
     * @throws IOException
     */
    protected void basicPublish(String exchangeName, String routingKey,
        BasicProperties props, byte[] message) {
        //write to exchange
        
        try {
            channel.basicPublish(exchangeName, routingKey, props, message);
            
            logger.info(String.format("basicPublish(%s, %s, ...)",
                    exchangeName, routingKey));
        } catch (IOException e) {
            logger.fatal(String.format( "Could not basicPublish(%s, %s, ...) -> %s",
                    exchangeName, routingKey, e.getCause()), e);
        }
    }
    
}
