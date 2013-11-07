package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.Session;
import cl.niclabs.cb.backend.SessionManager;
import cl.niclabs.cb.backend.Signer;
import cl.niclabs.cb.backend.methods.SignInitMethod;
import cl.niclabs.cb.jcrypto.SessionManagerImpl;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
        SessionManager sm = SessionManagerImpl.getInstance();
        Session session = sm.getSession(sessionHandler);

        if (session == null) {
            return ResponseMessage.ErrorMessage("Bad session handler.");
        }

        Signer signer = session.getSigner();
        try {
            signer.init(mechanism, handler);
            return ResponseMessage.OKMessage();
        }
        catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
        }
    }
}
