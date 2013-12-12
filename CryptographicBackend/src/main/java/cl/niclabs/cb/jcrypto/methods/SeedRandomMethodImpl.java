package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.Session;
import cl.niclabs.cb.backend.SessionManager;
import cl.niclabs.cb.backend.methods.SeedRandomMethod;
import cl.niclabs.cb.jcrypto.SessionManagerImpl;

import javax.xml.bind.DatatypeConverter;

public class SeedRandomMethodImpl implements SeedRandomMethod {
    private final String sessionHandler;
    private final byte[] seed;
    private final SessionManager sessionManager;

    public SeedRandomMethodImpl(Args args, SessionManager sessionManager) {
        sessionHandler = args.sessionHandler;
        seed = DatatypeConverter.parseBase64Binary(args.seed);
        this.sessionManager = sessionManager;
    }

    @Override
    public ResponseMessage execute() {
        Session session = sessionManager.getSession(sessionHandler);
        if (session == null) {
            return ResponseMessage.ErrorMessage("Bad session handler");
        }

        session.getRandomGenerator().setSeed(seed);
        return ResponseMessage.OKMessage();
    }
}
