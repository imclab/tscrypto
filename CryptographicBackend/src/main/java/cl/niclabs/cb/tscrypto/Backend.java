package cl.niclabs.cb.tscrypto;

import cl.inria.tscrypto.common.utils.TSConnection;
import cl.inria.tscrypto.common.utils.TSLogger;
import cl.inria.tscrypto.keyFactory.KeyDispatchRequestManager;
import cl.inria.tscrypto.keyFactory.KeyDispatcher;
import cl.inria.tscrypto.keyFactory.KeyManagementCollector;
import cl.inria.tscrypto.sigDealer.Dispatcher;
import cl.inria.tscrypto.sigDealer.RequestManager;
import cl.inria.tscrypto.sigDealer.ResultsCollector;
import cl.inria.tscrypto.sigDealer.SDConfig;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Backend extends Thread {

    private boolean running;
    private Dispatcher dispatcher;
    private KeyDispatcher keyDispatcher;
    private MethodCollector methodCollector;
    private ResultsCollector resultsCollector;
    private KeyManagementCollector keyManagementCollector;

    public Backend() throws IOException {
        SDConfig config = SDConfig.getInstance();
        Connection connection = TSConnection.getConnection(config.getRabbitMQConfig());

        TSLogger.sd.debug(String.format("Connected to RabbitMQ Server: %s", config.getRabbitMQConfig()));

        dispatcher = new Dispatcher(connection, config.getRabbitMQConfig());
        keyDispatcher = new KeyDispatcher(connection, config.getRabbitMQConfig());

        RequestManager requestManager = new RequestManager(
                dispatcher,
                config.getRabbitMQConfig().getClientQueue(),
                config.getRabbitMQConfig().getSignRequestAlias()
        );
        KeyDispatchRequestManager keyRequestManager =
                new KeyDispatchRequestManager(keyDispatcher);


        methodCollector = new MethodCollector(connection, requestManager, keyRequestManager);
        resultsCollector = new ResultsCollector(connection, requestManager);
        keyManagementCollector = new KeyManagementCollector(connection, keyRequestManager);
        running = true;

        TSLogger.sd.info("Initialization: Done");
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (running) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
            TSLogger.sd.fatal("Interrupted " + this, e);
            stopServer();
        }
    }

    private synchronized void stopServer()  {
        running = false;
        this.notifyAll();
        methodCollector.handleShutdownSignal(null, null);
        resultsCollector.handleShutdownSignal(null, null);
        keyManagementCollector.handleShutdownSignal(null, null);
        try {
            dispatcher.close();
            keyDispatcher.close();
        } catch (IOException e) {
            TSLogger.sd.fatal("Error while closing...");
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        Backend backend = new Backend();
        backend.start();
        backend.join();
    }
}
