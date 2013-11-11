package cl.niclabs.cb.tscrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.Session;
import cl.niclabs.cb.backend.SessionManager;
import cl.niclabs.cb.backend.Signer;
import cl.niclabs.cb.backend.methods.SignInitMethod;
import cl.niclabs.cb.jcrypto.SessionManagerImpl;

class SignInitMethodImpl implements SignInitMethod {
    private final String sessionHandler;
    private final String mechanism;
    private final String privateKeyHandler;

    public SignInitMethodImpl(Args args) {
        sessionHandler = args.sessionHandler;
        mechanism = args.mechanism;
        privateKeyHandler = args.keyHandler;
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
            signer.init(this.mechanism, this.privateKeyHandler);
            return ResponseMessage.OKMessage();
        } catch (Exception e) {
            return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
        }
    }
}
