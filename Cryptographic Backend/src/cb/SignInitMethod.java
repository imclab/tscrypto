package cb;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public class SignInitMethod implements Method {
	private String mechanism;
	private String privateKeyHandler;
	
	public SignInitMethod(String mechanism, String privateKeyHandler) {
		this.mechanism = mechanism;
		this.privateKeyHandler = privateKeyHandler;
	}
	
	@Override
	public ResponseMessage execute() {
		KeyStorage ks = MapKeyStorage.getInstance();
		PrivateKey pk = ks.getPrivateKey(Integer.parseInt(privateKeyHandler));
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

}
