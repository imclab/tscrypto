package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.SignInitMethod;
import cl.niclabs.cb.jcrypto.KeyStorage;
import cl.niclabs.cb.jcrypto.MapKeyStorage;
import cl.niclabs.cb.jcrypto.Signer;
import cl.niclabs.cb.jcrypto.SignerImpl;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

class SignInitMethodImpl implements SignInitMethod {
    protected final String mechanism;
    protected final String privateKeyHandler;

    public SignInitMethodImpl(Args args) {
        mechanism = args.mechanism;
        privateKeyHandler = args.handler;
    }

    @Override
    public ResponseMessage execute() {
        KeyStorage ks = MapKeyStorage.getInstance();
        String handler = privateKeyHandler;
        PrivateKey pk = ks.getPrivateKey(handler);
        if (pk != null) {
            Signer signer = SignerImpl.getInstance();
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
