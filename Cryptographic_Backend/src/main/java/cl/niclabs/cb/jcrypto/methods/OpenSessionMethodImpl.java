package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.OpenSessionMethod;
import cl.niclabs.cb.jcrypto.Session;
import cl.niclabs.cb.jcrypto.SessionManager;

public class OpenSessionMethodImpl implements OpenSessionMethod {

    @Override
    public ResponseMessage execute() {
        SessionManager sm = SessionManager.getInstance();
        Session session = new Session();
        sm.addSession(session);

        return ResponseMessage.OKMessage(new ReturnValue(session.getHandler()));
    }
}
