package cb.backend;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import com.google.gson.Gson;

public class SignInitMethod implements Method {
	public static class Args {
		public String mechanism, privateKeyHandler;
	}
	private String mechanism;
	private String privateKeyHandler;
	
	public SignInitMethod(String mechanism, String privateKeyHandler) {
		this.mechanism = mechanism;
		this.privateKeyHandler = privateKeyHandler;
	}
	
	public SignInitMethod(Args args) {
		this.mechanism = args.mechanism;
		this.privateKeyHandler = args.privateKeyHandler;
	}
	
	@Override
	public ResponseMessage execute() {
		KeyStorage ks = MapKeyStorage.getInstance();
		int handler = Integer.parseInt(privateKeyHandler);
		PrivateKey pk = ks.getPrivateKey(handler);
		if (pk != null) {
			Signer signer = Signer.getInstance();
			try {
				signer.init(mechanism, pk);
				return ResponseMessage.OKMessage(null);
			} catch (InvalidKeyException e) {
				return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
			}
			catch (NoSuchAlgorithmException e) {
				return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
			}
			
			
		} else {
			return ResponseMessage.ErrorMessage("Llave no encontrada.");
		}
	}
	
	public static void main (String[] args) {
		ResponseMessage rm;
		GenerateKeyPairMethod.ReturnValue rv;
		Gson gson = new Gson();
		GenerateKeyPairMethod gkpm = 
				new GenerateKeyPairMethod("hola", "mundo", "RSA", 1024);
		SignInitMethod sim;
		
		rm = gkpm.execute();
		System.out.println(rm);
		if (rm.getReturnCode().equals("OK")) {
			rv = gson.fromJson (rm.getValue(), GenerateKeyPairMethod.ReturnValue.class);
			
			sim = new SignInitMethod("SHA1withRSA", rv.privateKeyHandler);
			rm = sim.execute();
			System.out.println(rm);
			
		}
		
	}

}
