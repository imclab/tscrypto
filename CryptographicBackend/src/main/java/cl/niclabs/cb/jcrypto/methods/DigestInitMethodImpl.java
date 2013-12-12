package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.Digester;
import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.Session;
import cl.niclabs.cb.backend.SessionManager;
import cl.niclabs.cb.backend.methods.DigestInitMethod;
import cl.niclabs.cb.jcrypto.SessionManagerImpl;

import java.security.NoSuchAlgorithmException;


public class DigestInitMethodImpl implements DigestInitMethod {
    private final String sessionHandler;
    private final String mechanism;
    private final SessionManager sessionManager;

    public DigestInitMethodImpl(Args args, SessionManager sessionManager) {
        sessionHandler = args.sessionHandler;
        mechanism = args.mechanism;
        this.sessionManager = sessionManager;
    }

    @Override
    public ResponseMessage execute() {
        Session session = sessionManager.getSession(sessionHandler);
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
