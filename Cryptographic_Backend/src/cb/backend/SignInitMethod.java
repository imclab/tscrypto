package cb.backend;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import com.google.gson.Gson;

import javax.crypto.NoSuchPaddingException;

public class SignInitMethod implements Method {
	public static class Args {
		public String mechanism;
		public long handler;
	}
	private String mechanism;
	private long privateKeyHandler;
	
	public SignInitMethod(String mechanism, long privateKeyHandler) {
		this.mechanism = mechanism;
		this.privateKeyHandler = privateKeyHandler;
	}
	
	public SignInitMethod(Args args) {
		this.mechanism = args.mechanism;
		this.privateKeyHandler = args.handler;
	}
	
	@Override
	public ResponseMessage execute() {
		KeyStorage ks = MapKeyStorage.getInstance();
		long handler = privateKeyHandler;
		PrivateKey pk = ks.getPrivateKey(handler);
		if (pk != null) {
			Signer signer = Signer.getInstance();
			try {
				signer.init(mechanism, pk);
				return ResponseMessage.OKMessage(null);
			} 
			catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
				return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
            }

        }
		else {
			return ResponseMessage.ErrorMessage("Llave no encontrada.");
		}
	}
	
	public static void main (String[] args) {
		ResponseMessage rm;
		GenerateKeyPairMethod.ReturnValue rv;
		Gson gson = new Gson();
		GenerateKeyPairMethod gkpm = new GenerateKeyPairMethod("RSA", 1024);
		SignInitMethod sim;
		
		rm = gkpm.execute();
		System.out.println(rm);
		if (rm.getReturnCode().equals("OK")) {
			rv = gson.fromJson (rm.getValue(), GenerateKeyPairMethod.ReturnValue.class);
			
			sim = new SignInitMethod("SHA1withRSA", rv.handler);
			rm = sim.execute();
			System.out.println(rm);
			
		}
		
	}

}
