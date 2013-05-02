package cb;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class GenerateKeyPairMethod implements Method {
	private String label, id, keyType;
	private int keySize;
	
	private KeyStorage ks;
	
	public GenerateKeyPairMethod(String label, String id, String keyType, int keySize) {
		this.label = label;
		this.id = id;
		this.keyType = keyType;
		this.keySize = keySize;
		
		this.ks = MapKeyStorage.getInstance();
	}
	
	@Override
	public ResponseMessage execute() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(keyType);
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			kpg.initialize(keySize, random);
			
			KeyPair pair = kpg.generateKeyPair();
			int handler = ks.storeKeyPair(id, label, keyType, keySize, pair);
			
			ResponseMessage rm = ResponseMessage.OKMessage(
					"{\"publicKeyHandler\" : \"" + handler +
					",\"privateKeyHandler\" : \""+ handler + 
					"\"");
			return rm;
		}
		catch (NoSuchAlgorithmException e) {
			return ResponseMessage.ErrorMessage(e.getLocalizedMessage()); 
		}
	}

}
