package cl.inria.tscrypto.node;

import java.io.IOException;

import cl.inria.tscrypto.common.encryption.KeyChain;
import cl.inria.tscrypto.common.utils.TSConnection;
import cl.inria.tscrypto.common.utils.TSLogger;

import com.rabbitmq.client.Connection;

public class NodeController extends Thread {

	private NodeConfig config;
	private boolean running;
	private Dispatcher dispatcher;
	private Collector collector;
    private KeyShareManager keyManager;
    private KeyCollector keyCollector;

	public NodeController(NodeConfig config) throws IOException {
        this.config = config;

        // KeyChain.consoleSetup(config.getKeyStore());

        Connection connection = TSConnection.getConnection(config.getRabbitMQConfig());

        TSLogger.node.debug(String.format("Connected to RabbitMQ Server: %s", config.getRabbitMQConfig()));

        dispatcher = new Dispatcher(connection);
        keyManager = new KeyShareManager();
        collector = new Collector(this.config, connection, dispatcher, keyManager);
        keyCollector = new KeyCollector(config, connection, dispatcher, keyManager);

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
    public static void main(String[] args) throws IOException, InterruptedException {
		NodeController controller = new NodeController(new NodeConfig());
		controller.start();
		controller.join();
	}
}
