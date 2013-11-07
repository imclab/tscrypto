package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.SessionManager;
import cl.niclabs.cb.backend.methods.CloseSessionMethod;
import cl.niclabs.cb.jcrypto.SessionManagerImpl;

public class CloseSessionMethodImpl implements CloseSessionMethod {
    private String sessionHandler;

    public CloseSessionMethodImpl(Args args) {
        sessionHandler = args.sessionHandler;
    }

    @Override
    public ResponseMessage execute() {
        SessionManager sm = SessionManagerImpl.getInstance();
        sm.deleteSession(sessionHandler);
        return ResponseMessage.OKMessage();
    }
}
