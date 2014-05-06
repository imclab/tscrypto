package cl.niclabs.tscrypto.manager;

import cl.niclabs.tscrypto.common.datatypes.JsonFormat;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import org.zeromq.ZMQ;
import sun.security.ssl.RSASignature;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.Closeable;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;

import static java.security.KeyStore.PasswordProtection;
import static java.security.KeyStore.ProtectionParameter;

public class PublishDispatcher implements Dispatcher, Closeable{
    private ZMQ.Socket socket;

    public PublishDispatcher(ZMQ.Context context) {
        SDConfig config = SDConfig.getInstance();
        socket = context.socket(ZMQ.PUB);
        socket.bind("tcp://*:" + config.getDispatcherPort());
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    private byte[] signMessage(byte[] data) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, InvalidKeyException, SignatureException {
        SDConfig config = SDConfig.getInstance();
        KeyStore keyStore = config.getKeyStore();
        String alias = config.getKeyAlias();
        ProtectionParameter password = new PasswordProtection(config.getKeyPassword());

        KeyStore.PrivateKeyEntry privateKeyEntry =
                (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, password);
        PrivateKey privateKey = privateKeyEntry.getPrivateKey();

        Signature signature = Signature.getInstance("SHA256WithRSA");
        signature.initSign(privateKey);

        signature.update(data);
        return signature.sign();
    }

    @Override
    public void dispatch(String envelope, JsonFormat message) throws IOException {
        TSLogger.sd.debug("Sending message: envelope=" + envelope);
        TSLogger.sd.debug("Sending message: " + message.toJson());

        byte[] data = message.toJson().getBytes();
        byte[] signature;

        try {
            signature = signMessage(data);
        } catch (NoSuchAlgorithmException
                | UnrecoverableEntryException
                | KeyStoreException
                | InvalidKeyException
                | SignatureException e) {
            TSLogger.sd.error("Cannot sign the message.", e);
            signature = "".getBytes();
        }
        TSLogger.sd.debug("Sending message: signature=" + new BigInteger(signature));

        socket.sendMore(envelope.getBytes());
        socket.sendMore(data);
        socket.send(signature);
    }

    @Override
    public void dispatchEncrypted(String envelope, JsonFormat message) throws IOException {

    }
}
