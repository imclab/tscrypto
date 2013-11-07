package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.GenerateRandomMethod;
import cl.niclabs.cb.jcrypto.Session;
import cl.niclabs.cb.jcrypto.SessionManager;

import javax.xml.bind.DatatypeConverter;

public class GenerateRandomMethodImpl implements GenerateRandomMethod {
    private String sessionHandler;
    private int length;

    public GenerateRandomMethodImpl(Args args) {
        sessionHandler = args.sessionHandler;
        length = args.length;
    }

    @Override
    public ResponseMessage execute() {
        Session session = SessionManager.getInstance().getSession(sessionHandler);
        if (session == null) {
            return ResponseMessage.ErrorMessage("Bad session handler");
        }

        byte[] randomBytes = session.getRandomGenerator().generateRandom(this.length);
        String rv = DatatypeConverter.printBase64Binary(randomBytes);
        return ResponseMessage.OKMessage(new ReturnValue(rv));
    }
}
