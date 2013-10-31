package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.DeleteKeyPairMethod;
import cl.niclabs.cb.jcrypto.KeyStorage;
import cl.niclabs.cb.jcrypto.MapKeyStorage;

class DeleteKeyPairMethodImpl implements DeleteKeyPairMethod {
    protected final String handler;

    public DeleteKeyPairMethodImpl(Args args) {
        handler = args.handler;
    }

    @Override
    public ResponseMessage execute() {
        KeyStorage keyStorage = MapKeyStorage.getInstance();
        try {
            keyStorage.deleteKeyPair(handler);
            return ResponseMessage.OKMessage();
        } catch (Exception e) {
            return ResponseMessage.ErrorMessage(e.getMessage());
        }
    }
}
