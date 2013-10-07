package cl.niclabs.cb.backend.methods.implementation;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.DeleteKeyPairMethod;

class DeleteKeyPairMethodImpl extends DeleteKeyPairMethod {
    public DeleteKeyPairMethodImpl(Args args) {
        super(args);
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
