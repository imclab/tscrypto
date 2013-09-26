package cb.backend.methods;

import cb.backend.KeyStorage;
import cb.backend.MapKeyStorage;
import cb.backend.ResponseMessage;
import cb.backend.Signer;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

class SignInitMethodImpl extends SignInitMethod {
    public SignInitMethodImpl(Args args) {
        super(args);
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
}
