package cl.niclabs.cb.tscrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.SessionManager;
import cl.niclabs.cb.backend.methods.CloseSessionMethod;
import cl.niclabs.cb.jcrypto.SessionManagerImpl;

public class CloseSessionMethodImpl implements CloseSessionMethod {
    private String sessionHandler;
    private SessionManager sessionManager;

    public CloseSessionMethodImpl(Args args, SessionManager sessionManager) {
        sessionHandler = args.sessionHandler;
        this.sessionManager = sessionManager;
    }

    @Override
    public ResponseMessage execute() {
        SessionManager sm = sessionManager;
        sm.deleteSession(sessionHandler);
        return ResponseMessage.OKMessage();
    }
}
