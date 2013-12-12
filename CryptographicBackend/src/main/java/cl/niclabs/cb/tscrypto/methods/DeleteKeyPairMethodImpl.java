package cl.niclabs.cb.tscrypto.methods;

import cl.inria.tscrypto.sigDealer.KeyManager;
import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.DeleteKeyPairMethod;

class DeleteKeyPairMethodImpl implements DeleteKeyPairMethod {
    private final String handler;
    private final KeyManager keyManager;

    public DeleteKeyPairMethodImpl(Args args, KeyManager keyManager) {
        handler = args.keyHandler;
        this.keyManager = keyManager;
    }

    @Override
    public ResponseMessage execute() {
        keyManager.removeKey(this.handler);
        return ResponseMessage.OKMessage();
    }
}
