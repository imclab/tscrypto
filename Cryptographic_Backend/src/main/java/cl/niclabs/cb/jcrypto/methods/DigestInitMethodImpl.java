package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.Session;
import cl.niclabs.cb.backend.methods.DigestInitMethod;
import cl.niclabs.cb.backend.Digester;
import cl.niclabs.cb.jcrypto.SessionManagerImpl;

import java.security.NoSuchAlgorithmException;


public class DigestInitMethodImpl implements DigestInitMethod {
    private String sessionHandler;
    private String mechanism;

    public DigestInitMethodImpl(Args args) {
        sessionHandler = args.sessionHandler;
        mechanism = args.mechanism;
    }

    @Override
    public ResponseMessage execute() {
        Session session = SessionManagerImpl.getInstance().getSession(sessionHandler);
        if (session == null) {
            return ResponseMessage.ErrorMessage("Bad session handler");
        }

        Digester digester = session.getDigester();
        try {
            digester.digestInit(this.mechanism);
            return ResponseMessage.OKMessage();
        } catch (NoSuchAlgorithmException e) {
            return ResponseMessage.ErrorMessage(e.getMessage());
        }
    }
}
