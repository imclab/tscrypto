package cl.niclabs.cb.backend.methods.implementation;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.SignInitMethod;

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
        String handler = privateKeyHandler;
        PrivateKey pk = ks.getPrivateKey(handler);
        if (pk != null) {
            Signer signer = Signer.getInstance();
            try {
                signer.init(mechanism, pk);
                return ResponseMessage.OKMessage();
            }
            catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
                return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
            }

        } else {
            return ResponseMessage.ErrorMessage("Llave no encontrada.");
        }
    }
}
