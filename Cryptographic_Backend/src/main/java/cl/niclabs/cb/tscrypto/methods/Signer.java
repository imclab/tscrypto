package cl.niclabs.cb.tscrypto.methods;

import cl.inria.tscrypto.common.algorithms.SignatureRequest;
import cl.inria.tscrypto.common.datatypes.Ticket;
import cl.inria.tscrypto.node.KeyManager;
import cl.inria.tscrypto.sigDealer.RequestManager;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

public class Signer {

    private KeyManager keyManager;
    private String algorithm;
    private String privateKeyHandler;
    private RequestManager requestManager;

    public final static Set<String> algorithms = new HashSet<>();
    static {
        algorithms.add("Sha1WithRSA");
        algorithms.add("NONEWithRSA");
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

        if (!algorithms.contains(algorithm)) {
            throw new NoSuchAlgorithmException("No such algorithm: " + algorithm);
        }

        this.privateKeyHandler = privateKeyHandler;
	}
	
	public byte[] sign (byte[] data) throws Exception {
        String hashAlgorithm;
        switch(algorithm) {
            case "NONEWithRSA":
                hashAlgorithm = "NONE";
                break;
            case "Sha1WithRSA":
                hashAlgorithm = "Sha1";
                break;
            default:
                throw new Exception("Signer no iniciado.");
        }

        requestManager.addKey(keyManager.getKeyInfo(privateKeyHandler));
        Ticket ticket = requestManager.sign(hashAlgorithm, data, privateKeyHandler);

        SignatureRequest request = requestManager.getSignatureRequest(ticket);

        BigInteger signature = request.getSignature();

        while (signature == null) {
            synchronized (request) { // Double-checked locking... // TODO: Review if it's necessary
                while (signature == null) {
                    System.out.println("Waiting for signature ticket " + ticket.getId());
                    request.wait(5000);
                    signature = request.getSignature();
                }
            }
        }
        requestManager.removeKey(privateKeyHandler);

        return signature.toByteArray();

    }
	
}
