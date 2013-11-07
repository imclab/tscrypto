package cl.niclabs.cb.tscrypto;

import cl.inria.tscrypto.common.algorithms.SignatureRequest;
import cl.inria.tscrypto.common.datatypes.Ticket;
import cl.inria.tscrypto.node.KeyManager;
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

    private final static Map<String, String> algorithms = new Hashtable<String, String>() {{
        put("Sha1WithRSA", "Sha1");
        put("NONEWithRSA", "NONE");
    }};

    public SignerImpl(KeyManager keyManager, RequestManager requestManager) {
        this.keyManager = keyManager;
        this.requestManager = requestManager;
    }

	public void init (String algorithm, String privateKeyHandler)
            throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException
    {
        if (keyManager.getKeyInfo(privateKeyHandler) == null) {
            throw new InvalidKeyException("Not exists any key with handler: " + privateKeyHandler);
        }

        this.algorithm = algorithm;

        if (!algorithms.containsKey(algorithm)) {
            throw new NoSuchAlgorithmException("No such algorithm: " + algorithm);
        }

        this.privateKeyHandler = privateKeyHandler;
	}
	
	public byte[] sign (byte[] data) throws Exception {
        if(!algorithms.containsKey(algorithm)) {
            throw new Exception("SignerImpl no iniciado.");
        }

        String hashAlgorithm = algorithms.get(algorithm);

        requestManager.addKey(keyManager.getKeyInfo(privateKeyHandler));
        Ticket ticket = requestManager.sign(hashAlgorithm, data, privateKeyHandler);

        SignatureRequest request = requestManager.getSignatureRequest(ticket);

        BigInteger signature = request.getSignature();

        while (signature == null) {
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (request) { // Double-checked locking... // TODO: Review if it's necessary
                while (signature == null) {
                    System.out.println("Waiting for signature ticket " + ticket.getId());
                    request.wait(5000);
                    signature = request.getSignature();
                }
            }
        }
        requestManager.removeKey(privateKeyHandler);
        algorithm = "";
        privateKeyHandler = "";

        return signature.toByteArray();

    }
	
}
