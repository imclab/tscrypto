package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.jcrypto.SessionImpl;
import cl.niclabs.cb.backend.methods.OpenSessionMethod;
import cl.niclabs.cb.backend.SessionManager;
import cl.niclabs.cb.jcrypto.SessionManagerImpl;

public class OpenSessionMethodImpl implements OpenSessionMethod {

    @Override
    public ResponseMessage execute() {
        SessionManager sm = SessionManagerImpl.getInstance();
        SessionImpl session = new SessionImpl();
        sm.addSession(session);

        return ResponseMessage.OKMessage(new ReturnValue(session.getHandler()));
    }
}
