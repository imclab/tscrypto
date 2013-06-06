package cb.backend;

import com.google.gson.Gson;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FindKeyMethod implements Method {

    public static class ReturnValuePrivate {

        public int handler;
    }

    public static class ReturnValuePublic {

        public String key;
    }

    public static class Args {

        public String keyType;
        public String id;
    }
    private String keyType;
    private String id;

    public FindKeyMethod(Args args) {
        keyType = args.keyType;
        id = args.id;
    }

    @Override
    public ResponseMessage execute() {
        Gson gson = new Gson();
        switch (keyType) {
            case "publicKey": { // Las llaves son para el scope
                KeyStorage ks = MapKeyStorage.getInstance();
                PublicKey pk;
                try {
                    pk = ks.getPublicKey(ks.findHandler(id));
                } catch (Exception e) {
                    return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
                }

                ReturnValuePublic rv = new ReturnValuePublic();
                rv.key = Signer.bytesToHex(pk.getEncoded());
                return ResponseMessage.OKMessage(gson.toJson(rv));
            }
            case "privateKey": {
                ReturnValuePrivate rv = new ReturnValuePrivate();
                KeyStorage ks = MapKeyStorage.getInstance();
                try {
                    rv.handler = ks.findHandler(id);
                    return ResponseMessage.OKMessage(gson.toJson(rv));
                } catch (Exception e) {
                    return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
                }
            }
            default:
                throw new RuntimeException("Mal tipo de llave.");
        }

    }
}
