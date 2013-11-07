package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.DigestMethod;
import cl.niclabs.cb.jcrypto.Digester;
import cl.niclabs.cb.jcrypto.Session;
import cl.niclabs.cb.jcrypto.SessionManager;

import javax.xml.bind.DatatypeConverter;

public class DigestMethodImpl implements DigestMethod {
    private String sessionHandler;
    private byte[] data;

    public DigestMethodImpl(Args args) {
        sessionHandler = args.sessionHandler;
        data = DatatypeConverter.parseBase64Binary(args.data);
    }

    @Override
    public ResponseMessage execute() {
        Session session = SessionManager.getInstance().getSession(sessionHandler);
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
