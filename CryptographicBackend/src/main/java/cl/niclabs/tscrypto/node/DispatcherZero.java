package cl.niclabs.tscrypto.node;

import cl.niclabs.tscrypto.common.messages.TSMessage;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.manager.SDConfig;
import org.zeromq.ZMQ;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;

public class DispatcherZero implements Dispatcher, Closeable{
    private ZMQ.Socket socket;
    private NodeConfig config;

    public DispatcherZero(ZMQ.Context context) {
        config = NodeConfig.getInstance();
        socket = context.socket(ZMQ.PUSH);
        socket.connect("tcp://" + config.getManagerAddress() + ":" + config.getResultsPort());
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    private byte[] signMessage(byte[] data) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, InvalidKeyException, SignatureException {
        KeyStore keyStore = config.getKeyStore();
        String alias = config.getKeyAlias();
        KeyStore.ProtectionParameter password = new KeyStore.PasswordProtection(config.getKeyPassword());

        KeyStore.PrivateKeyEntry privateKeyEntry =
                (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, password);
        PrivateKey privateKey = privateKeyEntry.getPrivateKey();

        Signature signature = Signature.getInstance("SHA256WithRSA");
        signature.initSign(privateKey);

        signature.update(data);
        return signature.sign();
    }

    @Override
    public void send(TSMessage message) throws IOException {
        byte[] data = message.toJson().getBytes();
        byte[] nodeNumber = Integer.toString(config.getNodeId()).getBytes();
        byte[] signature;

        try {
            signature = signMessage(data);
        } catch (UnrecoverableEntryException
                | NoSuchAlgorithmException
                | KeyStoreException
                | InvalidKeyException
                | SignatureException e) {
            TSLogger.node.error("Cannot sign the message.", e);
            signature = "".getBytes();
        }

        TSLogger.node.debug("Sent Message: " + new String(data));
        TSLogger.node.debug("id=" + new String(nodeNumber));
        TSLogger.node.debug("Signature=" + new BigInteger(signature));

        socket.sendMore(data);
        socket.sendMore(nodeNumber);
        socket.send(signature);

    }
}
