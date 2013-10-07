package cl.niclabs.cb.tscrypto.methods;

import cl.inria.tscrypto.node.KeyManager;
import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.DeleteKeyPairMethod;

class DeleteKeyPairMethodImpl extends DeleteKeyPairMethod {
    private final KeyManager keyManager;

    public DeleteKeyPairMethodImpl(Args args, KeyManager keyManager) {
        super(args);
        this.keyManager = keyManager;
    }

    @Override
    public ResponseMessage execute() {
        keyManager.removeKey(this.handler);
        return ResponseMessage.OKMessage();
    }
}
