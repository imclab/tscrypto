package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.DigestInitMethod;
import cl.niclabs.cb.jcrypto.Digester;
import cl.niclabs.cb.jcrypto.DigesterImpl;

import java.security.NoSuchAlgorithmException;


public class DigestInitMethodImpl implements DigestInitMethod {
    protected final String mechanism;

    public DigestInitMethodImpl(Args args) {
        mechanism = args.mechanism;
    }

    @Override
    public ResponseMessage execute() {
        Digester digester = DigesterImpl.getInstance();
        try {
            digester.digestInit(this.mechanism);
            return ResponseMessage.OKMessage();
        } catch (NoSuchAlgorithmException e) {
            return ResponseMessage.ErrorMessage(e.getMessage());
        }
    }
}
