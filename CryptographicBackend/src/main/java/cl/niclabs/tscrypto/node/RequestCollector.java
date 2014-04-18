package cl.niclabs.tscrypto.node;

import cl.niclabs.tscrypto.common.datatypes.Collector;
import cl.niclabs.tscrypto.common.datatypes.EncryptedData;
import cl.niclabs.tscrypto.common.messages.*;
import cl.niclabs.tscrypto.common.utils.HandlerFactory;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.node.handlers.AddKeyHandler;
import cl.niclabs.tscrypto.node.handlers.DeleteKeyHandler;
import cl.niclabs.tscrypto.node.handlers.SignHandler;
import cl.niclabs.tscrypto.node.keyManagement.KeyShareManager;
import com.google.gson.JsonParseException;
import org.zeromq.ZMQ;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RequestCollector implements Collector, Closeable {
    private ZMQ.Socket socket;
    private Thread thread;
    private boolean running;
    private Dispatcher dispatcher;
    private KeyShareManager keyShareManager;

    private NodeConfig config;


    private final HandlerFactory<NodeHandler> handlerFactory;

    public RequestCollector(ZMQ.Context context, DispatcherZero dispatcher, KeyShareManager keyShareManager) {
        this.dispatcher = dispatcher;
        this.keyShareManager = keyShareManager;

        config = NodeConfig.getInstance();

        thread = null;
        socket = context.socket(ZMQ.SUB);
        socket.connect("tcp://" + config.getManagerAddress() + ":" + config.getDispatcherPort());
        socket.subscribe(config.getSignRequestEnvelope().getBytes());
        socket.subscribe(config.getKeyManagementEnvelope().getBytes());

        handlerFactory = new HandlerFactory<>();
        handlerFactory.addHandler(DeleteKeyQuery.class, DeleteKeyHandler.class);
        // handlerFactory.addHandler(SendKeyQuery.class, AddKeyHandler.class);
        handlerFactory.addHandler(EncryptedData.class, AddKeyHandler.class);
        handlerFactory.addHandler(SignShareQuery.class, SignHandler.class);

        running = false;
    }

    public void start() {
        if (!running) {
            running = true;
            thread = new CollectorThread();
            thread.start();
        }
    }

    public void stop() throws InterruptedException {
        if (running) {
            running = false;
            thread.join();
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    private class CollectorThread extends Thread {
        private Executor executor = Executors.newScheduledThreadPool(config.getNumThreads());
        @Override
        public void run() {
            while (running) {
                String message = socket.recvStr();
                TSLogger.node.debug("Received: " + message);
                executor.execute(new Handler(message));
            }
        }
    }

    private class Handler implements Runnable {
        final String message;

        private Handler(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            try {
                handleMessage(message);
            } catch (JsonParseException e) {
                TSLogger.node.debug("Parsing error for message: " + message, e);
            } catch (InstantiationException | IOException | ClassNotFoundException | IllegalAccessException e) {
                TSLogger.node.error("Unknown error for message: " + message, e);
            }
        }

        private void handleMessage(String messageStr) throws JsonParseException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
            TSMessage message = TSMessage.fromJson(messageStr);

            MessageAsync response =
                    handlerFactory
                            .createMessageHandler(message)
                            .handle(keyShareManager);

            if (response != null) {
                dispatcher.send(response, response.getReplyTo());
            }
        }
    }
}
