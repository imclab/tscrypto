package cb.backend.methods;

import cb.backend.KeyStorage;
import cb.backend.MapKeyStorage;
import cb.backend.ResponseMessage;

class DeleteKeyMethodImpl extends DeleteKeyMethod {
    public DeleteKeyMethodImpl(Args args) {
        super(args);
    }

    @Override
    public ResponseMessage execute() {
        KeyStorage keyStorage = MapKeyStorage.getInstance();
        try {
            keyStorage.deleteKeyPair(handler);
            return ResponseMessage.OKMessage(null);
        } catch (Exception e) {
            return ResponseMessage.ErrorMessage(e.getMessage());
        }
    }
}
