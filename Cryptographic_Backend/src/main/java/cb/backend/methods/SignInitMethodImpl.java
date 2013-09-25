package cb.backend.methods;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import cb.backend.*;
import com.google.gson.Gson;

import javax.crypto.NoSuchPaddingException;

public class SignInitMethodImpl implements Method {
	public static class Args {
		public String mechanism;
		public long handler;
	}
	private String mechanism;
	private long privateKeyHandler;
	
	public SignInitMethodImpl(String mechanism, long privateKeyHandler) {
		this.mechanism = mechanism;
		this.privateKeyHandler = privateKeyHandler;
	}
	
	public SignInitMethodImpl(Args args) {
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
		GenerateKeyPairMethodImpl.ReturnValue rv;
		Gson gson = new Gson();
		GenerateKeyPairMethodImpl gkpm = new GenerateKeyPairMethodImpl("RSA", 1024);
		SignInitMethodImpl sim;
		
		rm = gkpm.execute();
		System.out.println(rm);
		if (rm.getReturnCode().equals("OK")) {
			rv = gson.fromJson (rm.getValue(), GenerateKeyPairMethodImpl.ReturnValue.class);
			
			sim = new SignInitMethodImpl("SHA1withRSA", rv.handler);
			rm = sim.execute();
			System.out.println(rm);
			
		}
		
	}

}
