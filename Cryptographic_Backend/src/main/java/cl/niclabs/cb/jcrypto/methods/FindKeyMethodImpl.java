package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.FindKeyMethod;
import cl.niclabs.cb.jcrypto.KeyStorage;
import cl.niclabs.cb.jcrypto.MapKeyStorage;

import javax.xml.bind.DatatypeConverter;
import java.security.PublicKey;

class FindKeyMethodImpl implements FindKeyMethod {
    protected final String handler;

    public FindKeyMethodImpl(Args args) {
        handler = args.handler;
    }

    @Override
    public ResponseMessage execute() {
        KeyStorage ks = MapKeyStorage.getInstance();
        PublicKey pk;
        try {
            pk = ks.getPublicKey(handler);
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
