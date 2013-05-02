package cb;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;

public class Signer {
	final static private Signer instance = new Signer();
	public static Signer getInstance() {
		return instance;
	}
	
	private Signature sign = null;
	
	public void init (String algorithm, PrivateKey key) 
			throws NoSuchAlgorithmException, InvalidKeyException {

		sign = Signature.getInstance(algorithm);
		sign.initSign(key);
	}
	
}
