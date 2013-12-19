package cl.niclabs.cb.tscrypto;

import cl.inria.tscrypto.common.algorithms.SignatureRequest;
import cl.inria.tscrypto.common.datatypes.KeyMetaInfo;
import cl.inria.tscrypto.common.datatypes.TSPublicKey;
import cl.inria.tscrypto.common.datatypes.Ticket;
import cl.inria.tscrypto.sigDealer.KeyManager;
import cl.inria.tscrypto.sigDealer.RequestManager;
import cl.niclabs.cb.backend.Signer;

import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.Map;

public class SignerImpl implements Signer {

    private final KeyManager keyManager;
    private String algorithm;
    private String privateKeyHandler;
    private final RequestManager requestManager;
    private boolean initialized;

    private final static Map<String, String> algorithms = new Hashtable<String, String>() {{
        put("Sha1WithRSA", "Sha1");
        put("NONEWithRSA", "NONE");
    }};

    public SignerImpl(KeyManager keyManager, RequestManager requestManager) {
        this.keyManager = keyManager;
        this.requestManager = requestManager;
        initialized = false;
    }

	public void init (String algorithm, String keyHandler)
            throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException
    {
        if (keyManager.containsKey(keyHandler)) {
            throw new InvalidKeyException("Not exists any key with handler: " + keyHandler);
        }

        this.algorithm = algorithm;

        if (!algorithms.containsKey(algorithm)) {
            throw new NoSuchAlgorithmException("No such algorithm: " + algorithm);
        }

        this.privateKeyHandler = keyHandler;

        initialized = true;
	}
	
	public byte[] sign (byte[] data) throws Exception {
        if(!initialized) {
            throw new Exception("SignerImpl no iniciado.");
        }

        String hashAlgorithm = algorithms.get(algorithm);

        KeyMetaInfo keyMetaInfo = keyManager.getKeyMetaInfo(privateKeyHandler);
        TSPublicKey publicKey = keyManager.getPublicKey(privateKeyHandler);

        Ticket ticket = requestManager.sign(keyMetaInfo, publicKey, hashAlgorithm, data, privateKeyHandler);

        SignatureRequest request = requestManager.getSignatureRequest(ticket);
        request.waitUntilReady();
        BigInteger signature = request.getSignature();

        algorithm = "";
        privateKeyHandler = "";

        return signature.toByteArray();

    }
	
}
