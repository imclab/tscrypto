package cl.niclabs.cb.tscrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.SignInitMethod;

class SignInitMethodImpl extends SignInitMethod {
    private Signer signer;

    public SignInitMethodImpl(Args args, Signer signer) {
        super(args);
        this.signer = signer;
    }

    @Override
    public ResponseMessage execute() {

        try {
            signer.init(this.mechanism, this.privateKeyHandler);
            return ResponseMessage.OKMessage();
        } catch (Exception e) {
            return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
        }

    }
}
