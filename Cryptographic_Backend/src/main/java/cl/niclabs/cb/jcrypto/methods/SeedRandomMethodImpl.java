package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.SeedRandomMethod;
import cl.niclabs.cb.jcrypto.Session;
import cl.niclabs.cb.jcrypto.SessionManager;

import javax.xml.bind.DatatypeConverter;

public class SeedRandomMethodImpl implements SeedRandomMethod {
    private String sessionHandler;
    private byte[] seed;

    public SeedRandomMethodImpl(Args args) {
        sessionHandler = args.sessionHandler;
        seed = DatatypeConverter.parseBase64Binary(args.seed);
    }

    @Override
    public ResponseMessage execute() {
        Session session = SessionManager.getInstance().getSession(sessionHandler);
        if (session == null) {
            return ResponseMessage.ErrorMessage("Bad session handler");
        }

        session.getRandomGenerator().setSeed(seed);
        return ResponseMessage.OKMessage();
    }
}
