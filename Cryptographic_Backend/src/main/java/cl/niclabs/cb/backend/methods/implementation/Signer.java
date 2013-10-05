package cl.niclabs.cb.backend.methods.implementation;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;

public class Signer {
	final static private Signer instance = new Signer();
	public static Signer getInstance() {
		return instance;
	}

    private String algorithm = null;
    private Cipher cipher = null;
	private Signature signature = null;
	
	public void init (String algorithm, PrivateKey key)
            throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
        this.algorithm = algorithm;
        switch (algorithm) {
            case "RSA":
                cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, key);
                break;
            default:
                signature = Signature.getInstance(algorithm);
                signature.initSign(key);
                break;
        }

	}
	
	public byte[] sign (byte[] data) throws Exception {
        byte[] signedData;
        switch(algorithm) {
            case "RSA":
                if (cipher == null) {
                    throw new Exception("Cipher no iniciado");
                }
                else {
                    signedData = cipher.doFinal(data);
                    cipher = null;
                    return signedData;
                }
            default:
                if (signature == null) {
                    throw new Exception("Signature no iniciada");
                }
                else {
                    signature.update(data);
                    signedData = signature.sign();
                    signature = null;
                    return signedData;
                }
        }
    }
	
}
