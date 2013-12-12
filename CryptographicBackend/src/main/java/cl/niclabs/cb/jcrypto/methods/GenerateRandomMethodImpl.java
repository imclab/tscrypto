package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.Session;
import cl.niclabs.cb.backend.SessionManager;
import cl.niclabs.cb.backend.methods.GenerateRandomMethod;
import cl.niclabs.cb.jcrypto.SessionManagerImpl;

import javax.xml.bind.DatatypeConverter;

public class GenerateRandomMethodImpl implements GenerateRandomMethod {
    private final String sessionHandler;
    private final int length;
    private final SessionManager sessionManager;

    public GenerateRandomMethodImpl(Args args, SessionManager sessionManager) {
        sessionHandler = args.sessionHandler;
        length = args.length;
        this.sessionManager = sessionManager;
    }

    @Override
    public ResponseMessage execute() {
        Session session = sessionManager.getSession(sessionHandler);
        if (session == null) {
            return ResponseMessage.ErrorMessage("Bad session handler");
        }

        byte[] randomBytes = session.getRandomGenerator().generateRandom(this.length);
        String rv = DatatypeConverter.printBase64Binary(randomBytes);
        return ResponseMessage.OKMessage(new ReturnValue(rv));
    }
}
