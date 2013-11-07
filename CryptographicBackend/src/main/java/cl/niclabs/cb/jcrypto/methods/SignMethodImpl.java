package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.Session;
import cl.niclabs.cb.backend.Signer;
import cl.niclabs.cb.backend.methods.SignMethod;
import cl.niclabs.cb.jcrypto.SessionManagerImpl;

import javax.xml.bind.DatatypeConverter;

class SignMethodImpl implements SignMethod {
    private String sessionHandler;
    private byte[] data;

    public SignMethodImpl(Args args) {
        sessionHandler = args.sessionHandler;
        data = DatatypeConverter.parseBase64Binary(args.data);
    }

    @Override
    public ResponseMessage execute() {
        Session session = SessionManagerImpl.getInstance().getSession(sessionHandler);
        if (session == null) {
            return ResponseMessage.ErrorMessage("Bad session handler");
        }

        Signer signer = session.getSigner();
        try {
            ReturnValue rv = new ReturnValue(sessionHandler, DatatypeConverter.printBase64Binary(signer.sign(data)));
            return ResponseMessage.OKMessage(rv);
        } catch (Exception e) {
            return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
        }
    }
}
