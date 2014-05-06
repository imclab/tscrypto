package cl.niclabs.tscrypto.node;

import cl.niclabs.tscrypto.common.datatypes.Collector;
import cl.niclabs.tscrypto.common.messages.EncryptedData;
import cl.niclabs.tscrypto.common.messages.*;
import cl.niclabs.tscrypto.common.utils.HandlerFactory;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.node.handlers.AddKeyHandler;
import cl.niclabs.tscrypto.node.handlers.DeleteKeyHandler;
import cl.niclabs.tscrypto.node.handlers.EncryptedDataHandler;
import cl.niclabs.tscrypto.node.handlers.SignHandler;
import cl.niclabs.tscrypto.node.keyManagement.KeyShareManager;
import com.google.gson.JsonParseException;
import org.zeromq.ZMQ;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RequestCollector implements Collector, Closeable {
    private ZMQ.Socket socket;
    private ZMQ.Socket internodeSocket;
    private Thread thread;
    private boolean running;
    private Dispatcher dispatcher;
    private KeyShareManager keyShareManager;

    private NodeConfig config;


    public static final HandlerFactory<NodeHandler> handlerFactory = new HandlerFactory<>();
    static {
        handlerFactory.addHandler(DeleteKeyQuery.class, DeleteKeyHandler.class);
        handlerFactory.addHandler(SendKeyQuery.class, AddKeyHandler.class);
        handlerFactory.addHandler(SignShareQuery.class, SignHandler.class);
        handlerFactory.addHandler(EncryptedData.class, EncryptedDataHandler.class);
    }

    public RequestCollector(ZMQ.Context context, DispatcherZero dispatcher, KeyShareManager keyShareManager) {
        this.dispatcher = dispatcher;
        this.keyShareManager = keyShareManager;

        config = NodeConfig.getInstance();

        thread = null;
        socket = context.socket(ZMQ.SUB);
        socket.connect("tcp://" + config.getManagerAddress() + ":" + config.getDispatcherPort());
        socket.subscribe(config.getSignRequestEnvelope().getBytes());
        socket.subscribe(config.getKeyManagementEnvelope().getBytes());

        TSLogger.node.debug("Subscribed to: " + config.getSignRequestEnvelope());
        TSLogger.node.debug("Subscribed to: " + config.getKeyManagementEnvelope());

        internodeSocket = context.socket(ZMQ.REP);
        internodeSocket.setIdentity(config.getIdentity());
        internodeSocket.connect("tcp://" + config.getManagerAddress() + ":" + config.getIncomingRoutingPort());

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

        private boolean verify(KeyStore keyStore, byte[] data, byte[] signature)
                throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException,
                InvalidKeyException, SignatureException {
            Signature verifier = Signature.getInstance("SHA256WithRSA");
            Certificate certificate = keyStore.getCertificate(config.getManagerCertAlias());
            verifier.initVerify(certificate);
            verifier.update(data);
            return verifier.verify(signature);
        }

        @Override
        public void run() {
            ZMQ.Poller poller = new ZMQ.Poller(2);
            poller.register(socket, ZMQ.Poller.POLLIN); // index = 0;
            poller.register(internodeSocket, ZMQ.Poller.POLLIN); // index = 1;
            while (running) {
                poller.poll();
                if (poller.pollin(0)) { // socket
                    String envelope = socket.recvStr();
                    String message = socket.recvStr();
                    byte[] signature = socket.recv();
                    TSLogger.node.debug("Received: " + message + " From: " + envelope);
                    TSLogger.node.debug("Signature =" + new BigInteger(signature));
                    KeyStore keyStore = config.getKeyStore();
                    if (keyStore != null) {
                        try {
                            if(verify(keyStore, message.getBytes(), signature)) {
                                executor.execute(new Handler(message));
                            } else {
                                TSLogger.node.debug("Message discarded.");
                            }
                        } catch (KeyStoreException e) {
                            TSLogger.node.fatal("Cannot get manager certificate. Is it configured correctly?", e);
                        } catch (NoSuchProviderException | NoSuchAlgorithmException e) {
                            TSLogger.node.fatal("Cannot get signature verifier.", e);
                        } catch (InvalidKeyException | SignatureException e) {
                            TSLogger.node.fatal("Cannot initialize verifier.", e);
                        }
                    } else {
                        TSLogger.node.fatal("Cannot get the keystore. Is it configured correctly?");
                    }
                }
                if (poller.pollin(1)) { // internodeSocket
                    byte[] who = internodeSocket.recv();
                    String data = internodeSocket.recvStr();
                    TSLogger.node.debug("Received: " + data);
                    internodeSocket.sendMore(who);
                    internodeSocket.send("RECEIVED");
                }
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
                dispatcher.send(response);
            }
        }
    }
}
