package cl.niclabs.tscrypto.node.handlers;

import cl.niclabs.tscrypto.common.messages.EncryptedData;
import cl.niclabs.tscrypto.common.messages.*;
import cl.niclabs.tscrypto.common.utils.HandlerFactory;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.node.NodeHandler;
import cl.niclabs.tscrypto.node.RequestCollector;
import cl.niclabs.tscrypto.node.keyManagement.KeyShareManager;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

// Proxy handler!.
public class EncryptedDataHandler implements NodeHandler {
    private HandlerFactory<NodeHandler> handlerFactory = RequestCollector.handlerFactory;
    private EncryptedData encryptedData;

    @Override
    public MessageAsync handle(KeyShareManager context) {
        try {
            String json = new String(encryptedData.decrypt());
            TSMessage message = TSMessage.fromJson(json);
            TSLogger.node.debug("Decrypted message:" + message.toJson());
            return handlerFactory.createMessageHandler(message).handle(context);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace(); // TODO: better error handling
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ignore) {
        }
        return null;
    }

    @Override
    public void init(TSMessage message) {
        encryptedData = (EncryptedData) message;
    }
}
