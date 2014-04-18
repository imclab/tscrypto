package cl.niclabs.cb.tscrypto;

import cl.niclabs.cb.common.KeyOperations;
import cl.niclabs.tscrypto.common.algorithms.keyfactory.KeyFactory;
import cl.niclabs.tscrypto.common.datatypes.KeyInfo;
import cl.niclabs.tscrypto.common.datatypes.Ticket;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.manager.requests.DeleteKeyRequest;
import cl.niclabs.tscrypto.manager.requests.KeyDispatchRequest;
import cl.niclabs.tscrypto.manager.keyManagement.KeyFactoryFactory;
import cl.niclabs.tscrypto.manager.keyManagement.KeyManager;
import cl.niclabs.tscrypto.manager.RequestManager;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class KeyOperationsImpl implements KeyOperations {
    private final int k;
    private final int l;
    private final RequestManager requestManager;
    private final KeyManager keyManager;

    public KeyOperationsImpl(int k, int l, RequestManager requestManager, KeyManager keyManager) {
        this.k = k;
        this.l = l;
        this.requestManager = requestManager;
        this.keyManager = keyManager;
    }

    @Override
    public GenerationResult generateKeys(String keyType, int keySize, String publicExponent) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, IOException, InterruptedException {
        TSLogger.keyDealer.debug("KeyPair Generation with k="+k+" and l="+l);
        KeyFactory keyFactory = KeyFactoryFactory.getInstance();
        KeyInfo keyInfo = keyFactory.generateKeys(keySize / 2, k, l);
        TSLogger.keyDealer.debug("KeyPair Generation successful");
        String handler = keyInfo.getKeyMetaInfo().getAlias();
        Ticket ticket = requestManager.dispatchKey(keyInfo);
        KeyDispatchRequest request = requestManager.getKeyDispatchRequest(ticket);

        request.waitUntilReady();

        requestManager.removeRequest(ticket);
        keyManager.addKey(keyInfo);
        String modulus = DatatypeConverter.printBase64Binary(keyInfo.getPublicKey().getModulus().toByteArray());

        publicExponent = DatatypeConverter.printBase64Binary(keyInfo.getPublicKey().getExponent().toByteArray());
        return new GenerationResult(handler, modulus, publicExponent);
    }

    @Override
    public void deleteKeys(String keyHandler) throws Exception {
        Ticket ticket = requestManager.deleteKey(keyHandler);
        DeleteKeyRequest request = requestManager.getDeleteKeyRequest(ticket);
        request.waitUntilReady();
        requestManager.removeRequest(ticket);
        keyManager.removeKey(keyHandler);
    }

    @Override
    public String findKey(String handler) throws InvalidKeySpecException, NoSuchAlgorithmException {
        PublicKey pk = keyManager.getPublicKey(handler).convertoToPublicKey();

        String key = "-----BEGIN PUBLIC KEY-----\n";
        String keyBase64 = DatatypeConverter.printBase64Binary(pk.getEncoded());
        while(keyBase64.length() > 0) {
            try {
                key += keyBase64.substring(0, 64) + "\n";
                keyBase64 = keyBase64.substring(64);
            } catch (IndexOutOfBoundsException e) {
                key += keyBase64 + "\n";
                break;
            }
        }
        key += "-----END PUBLIC KEY-----\n";

        return key;
    }
}
