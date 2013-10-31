package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.DigestMethod;
import cl.niclabs.cb.jcrypto.Digester;
import cl.niclabs.cb.jcrypto.DigesterImpl;

import javax.xml.bind.DatatypeConverter;

public class DigestMethodImpl implements DigestMethod {
    protected final byte[] data;

    public DigestMethodImpl(Args args) {
        data = DatatypeConverter.parseBase64Binary(args.data);
    }

    @Override
    public ResponseMessage execute() {
        Digester digester = DigesterImpl.getInstance();
        try {
            String digest = DatatypeConverter.printBase64Binary(digester.digest(this.data));
            return ResponseMessage.OKMessage(new ReturnValue(digest));
        } catch (Exception e) {
            return ResponseMessage.ErrorMessage(e.getMessage());
        }
    }
}
