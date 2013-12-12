package cl.niclabs.cb.tscrypto.methods;

import cl.inria.tscrypto.sigDealer.KeyManager;
import cl.inria.tscrypto.sigDealer.RequestManager;
import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.Session;
import cl.niclabs.cb.backend.SessionManager;
import cl.niclabs.cb.backend.methods.OpenSessionMethod;
import cl.niclabs.cb.jcrypto.SessionManagerImpl;
import cl.niclabs.cb.tscrypto.SessionImpl;

public class OpenSessionMethodImpl implements OpenSessionMethod {
    private KeyManager keyManager;
    private RequestManager requestManager;
    private SessionManager sessionManager;

    public OpenSessionMethodImpl(KeyManager keyManager, RequestManager requestManager, SessionManager sessionManager) {
        this.keyManager = keyManager;
        this.requestManager = requestManager;
        this.sessionManager = sessionManager;
    }

    @Override
    public ResponseMessage execute() {
        SessionManager sm = sessionManager;
        Session session = new SessionImpl(keyManager, requestManager);
        sm.addSession(session);

        return ResponseMessage.OKMessage(new ReturnValue(session.getHandler()));
    }
}
