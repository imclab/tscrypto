package cl.niclabs.cb.tscrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.Session;
import cl.niclabs.cb.backend.SessionManager;
import cl.niclabs.cb.backend.Signer;
import cl.niclabs.cb.backend.methods.SignMethod;
import cl.niclabs.cb.jcrypto.SessionManagerImpl;

import javax.xml.bind.DatatypeConverter;

class SignMethodImpl implements SignMethod {
    private final String sessionHandler;
    private final byte[] data;
    private final SessionManager sessionManager;

    public SignMethodImpl(Args args, SessionManager sessionManager) {
        sessionHandler = args.sessionHandler;
        data = DatatypeConverter.parseBase64Binary(args.data);

        this.sessionManager = sessionManager;
    }

    @Override
    public ResponseMessage execute() {
        SessionManager sm = sessionManager;
        Session session = sm.getSession(sessionHandler);

        if (session == null) {
            return ResponseMessage.ErrorMessage("Bad session handler.");
        }

        Signer signer = session.getSigner();
        try {
            byte[] signature = signer.sign(this.data);
            ReturnValue rv = new ReturnValue(sessionHandler, DatatypeConverter.printBase64Binary(signature));
            return ResponseMessage.OKMessage(rv);

        } catch (Exception e) {
            return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
        }
    }
}
