package cl.niclabs.cb.tscrypto.methods;

import cl.inria.tscrypto.common.algorithms.SignatureRequest;
import cl.inria.tscrypto.common.datatypes.Ticket;
import cl.inria.tscrypto.node.KeyManager;
import cl.inria.tscrypto.sigDealer.RequestManager;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.Map;

public class Signer {

    private final KeyManager keyManager;
    private String algorithm;
    private String privateKeyHandler;
    private final RequestManager requestManager;

    private final static Map<String, String> algorithms = new Hashtable<>();
    static {
        algorithms.put("Sha1WithRSA", "Sha1");
        algorithms.put("NONEWithRSA", "NONE");
    }

    public Signer(KeyManager keyManager, RequestManager requestManager) {
        this.keyManager = keyManager;
        this.requestManager = requestManager;
    }

	public void init (String algorithm, String privateKeyHandler) throws Exception {
        if (keyManager.getKeyInfo(privateKeyHandler) == null) {
            throw new Exception("Not exists any key with handler: " + privateKeyHandler);
        }

        this.algorithm = algorithm;

        if (!algorithms.containsKey(algorithm)) {
            throw new NoSuchAlgorithmException("No such algorithm: " + algorithm);
        }

        this.privateKeyHandler = privateKeyHandler;
	}
	
	public byte[] sign (byte[] data) throws Exception {
        if(!algorithms.containsKey(algorithm)) {
            throw new Exception("Signer no iniciado.");
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
