package cl.niclabs.cb.tscrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.SignMethod;

import javax.xml.bind.DatatypeConverter;

class SignMethodImpl extends SignMethod {
    Signer signer;
    public SignMethodImpl(Args args, Signer signer) {
        super(args);
        this.signer = signer;
    }

    @Override
    public ResponseMessage execute() {
        try {
            byte[] signature = signer.sign(this.data);
            ReturnValue rv = new ReturnValue(DatatypeConverter.printBase64Binary(signature));
            return ResponseMessage.OKMessageWithObject(rv);

        } catch (Exception e) {
            return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
        }
    }
}
