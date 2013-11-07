package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.SignInitMethod;
import cl.niclabs.cb.jcrypto.*;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

class SignInitMethodImpl implements SignInitMethod {
    private String sessionHandler;
    private String mechanism;
    private String handler;


    public SignInitMethodImpl(Args args) {
        sessionHandler = args.sessionHandler;
        mechanism = args.mechanism;
        handler = args.handler;
    }

    @Override
    public ResponseMessage execute() {
        SessionManager sm = SessionManager.getInstance();
        Session session = sm.getSession(sessionHandler);

        if (session == null) {
            return ResponseMessage.ErrorMessage("Bad session handler.");
        }

        KeyStorage ks = MapKeyStorage.getInstance();
        PrivateKey pk = ks.getPrivateKey(handler);
        if (pk != null) {
            Signer signer = session.getSigner();
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
