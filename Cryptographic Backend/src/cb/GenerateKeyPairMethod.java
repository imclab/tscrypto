package cb;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.google.gson.Gson;

class GenerateKeyPairReturnValue {
	String publicKeyHandler;
	String privateKeyHandler;
}

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
		/* Forma de usar gson bknmente. */
		GenerateKeyPairReturnValue rv = new GenerateKeyPairReturnValue();
		
		Gson gson = new Gson();
		
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(keyType);
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			kpg.initialize(keySize, random);
			
			KeyPair pair = kpg.generateKeyPair();
			int handler = ks.storeKeyPair(id, label, keyType, keySize, pair);
			
			// Defino el return value, para ser luego serializado en json.
			rv.privateKeyHandler = rv.publicKeyHandler = Integer.toString(handler);
			
			ResponseMessage rm = ResponseMessage.OKMessage(gson.toJson(rv));
			return rm;
		}
		catch (NoSuchAlgorithmException e) {
			return ResponseMessage.ErrorMessage(e.getLocalizedMessage()); 
		}
	}
	
	public static void main(String[] args) {
		GenerateKeyPairMethod gkpm = 
				new GenerateKeyPairMethod("hola", "mundo", "RSA", 1024);
		ResponseMessage rm = gkpm.execute();
		System.out.println(rm);
	}

}
