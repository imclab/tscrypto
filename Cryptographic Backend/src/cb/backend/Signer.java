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
	
	final static public String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b: bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}
	
	final static public byte[] hexToBytes(String ss) {
		char[] s = ss.toCharArray();
		int len = s.length;
		byte[] data = new byte[len/2];
		
		for (int i=0; i < len; i+=2) {
			data[i/2] = (byte) ((Character.digit(s[i], 16) << 4)
					+ Character.digit(s[i+1], 16));
		}
		
		return data;
	}
	
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
