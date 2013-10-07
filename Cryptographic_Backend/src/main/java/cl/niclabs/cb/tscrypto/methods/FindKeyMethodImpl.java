package cl.niclabs.cb.tscrypto.methods;

import cl.inria.tscrypto.node.KeyManager;
import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.FindKeyMethod;

import javax.xml.bind.DatatypeConverter;
import java.security.PublicKey;

class FindKeyMethodImpl extends FindKeyMethod {
    private final KeyManager keyManager;

    public FindKeyMethodImpl(Args args, KeyManager keyManager) {
        super(args);
        this.keyManager = keyManager;
    }

    @Override
    public ResponseMessage execute() {
        PublicKey pk;
        try {
            pk = keyManager.getPublicKey(handler).convertoToPublicKey();
        } catch (Exception e) {
            return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
        }

        String key = "-----BEGIN PUBLIC KEY-----\n";
        String keyBase64 = DatatypeConverter.printBase64Binary(pk.getEncoded());
        while(keyBase64.length() > 0) {
            try {
                key += keyBase64.substring(0, 64) + "\n";
                keyBase64 = keyBase64.substring(64);
            } catch (IndexOutOfBoundsException e) {
                key += keyBase64 + "\n";
                break;
            }
        }
        key += "-----END PUBLIC KEY-----\n";

        return ResponseMessage.OKMessage(new ReturnValue(key));
    }
}
