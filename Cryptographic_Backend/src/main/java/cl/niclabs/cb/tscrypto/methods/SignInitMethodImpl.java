package cl.niclabs.cb.tscrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.SignInitMethod;

class SignInitMethodImpl implements SignInitMethod {
    protected final String mechanism;
    protected final String privateKeyHandler;
    private final Signer signer;

    public SignInitMethodImpl(Args args, Signer signer) {
        mechanism = args.mechanism;
        privateKeyHandler = args.handler;
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
