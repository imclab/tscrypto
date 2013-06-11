package cb.backend;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;

public class Signer {
	final static private Signer instance = new Signer();
	public static Signer getInstance() {
		return instance;
	}
	
	private Signature signature = null;
	
	public void init (String algorithm, PrivateKey key) 
			throws NoSuchAlgorithmException, InvalidKeyException {

		signature = Signature.getInstance(algorithm);
		signature.initSign(key);
	}
	
	public byte[] sign (byte[] data) throws Exception {
		if (signature == null)
			throw new Exception("Signature no iniciada");
		else {
			signature.update(data);
			return signature.sign();
		}
	}
	
}
