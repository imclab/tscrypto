package cl.inria.tscrypto.node;

import java.io.IOException;
import java.sql.SQLException;

import cl.inria.tscrypto.common.config.RabbitMQConfig;
import cl.inria.tscrypto.common.utils.TSConnection;
import cl.inria.tscrypto.common.utils.TSLogger;

import com.rabbitmq.client.Connection;

public class NodeController extends Thread {

	private boolean running;
	private Dispatcher dispatcher;
	private Collector collector;
    private KeyShareManager keyManager;
    private KeyCollector keyCollector;

	public NodeController() throws IOException, SQLException, ClassNotFoundException {
        RabbitMQConfig rconfig = NodeConfig.getInstance().getRabbitMQConfig();

        // KeyChain.consoleSetup(config.getKeyStore());

        Connection connection = TSConnection.getConnection(rconfig);
        TSLogger.node.debug(String.format("Connected to RabbitMQ Server: %s", rconfig));

        dispatcher = new Dispatcher(connection);
        keyManager = new H2KeyShareManager();
        collector = new Collector(connection, dispatcher, keyManager);
        keyCollector = new KeyCollector(connection, dispatcher, keyManager);

        running = true;

        TSLogger.node.debug("Initialization: Done");
    }

    @Override
    public void run() {
        TSLogger.node.debug("Running...");

        try {
            synchronized (this) {
                while (running) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
            TSLogger.node.fatal(
                "Interrupted " + this, e);

            stopServer();
        }
    }

    public synchronized void stopServer() {
        running = false;
        notifyAll();

        collector.handleShutdownSignal(null, null);
        keyCollector.handleShutdownSignal(null, null);
    }
    
    /**
     * Non-daemon mode.
     * Cannot be controlled nor stopped.
     * Should be used for testing only
     * @param args
     * @throws IOException
     * @throws Exception
     */
    public static void main(String[] args) throws IOException, InterruptedException, SQLException, ClassNotFoundException {
		NodeController controller = new NodeController();
		controller.start();
		controller.join();
	}
}
