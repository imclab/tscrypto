package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.Digester;
import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.Session;
import cl.niclabs.cb.backend.SessionManager;
import cl.niclabs.cb.backend.methods.DigestMethod;
import cl.niclabs.cb.jcrypto.SessionManagerImpl;

import javax.xml.bind.DatatypeConverter;

public class DigestMethodImpl implements DigestMethod {
    private final String sessionHandler;
    private final byte[] data;
    private final SessionManager sessionManager;

    public DigestMethodImpl(Args args, SessionManager sessionManager) {
        sessionHandler = args.sessionHandler;
        data = DatatypeConverter.parseBase64Binary(args.data);
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
            String digest = DatatypeConverter.printBase64Binary(digester.digest(this.data));
            return ResponseMessage.OKMessage(new ReturnValue(digest));
        } catch (Exception e) {
            return ResponseMessage.ErrorMessage(e.getMessage());
        }
    }
}
