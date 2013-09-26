package cb.backend.methods.implementation;

import cb.backend.KeyStorage;
import cb.backend.MapKeyStorage;
import cb.backend.ResponseMessage;
import cb.backend.methods.FindKeyMethod;
import com.google.gson.Gson;

import javax.xml.bind.DatatypeConverter;
import java.security.PublicKey;

class FindKeyMethodImpl extends FindKeyMethod {
    public FindKeyMethodImpl(Args args) {
        super(args);
    }

    @Override
    public ResponseMessage execute() {
        Gson gson = new Gson();
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
        ReturnValue rv = new ReturnValue(key);
        return ResponseMessage.OKMessage(gson.toJson(rv));
    }
}
