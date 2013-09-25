package cb.backend.methods;

import cb.backend.KeyStorage;
import cb.backend.MapKeyStorage;
import cb.backend.Method;
import cb.backend.ResponseMessage;
import com.google.gson.Gson;
import java.security.PublicKey;
import javax.xml.bind.DatatypeConverter;

public class FindKeyMethodImpl implements Method {

  public static class ReturnValue {

    public String key;
  }

  public static class Args {
    public long handler;
  }
  private long handler;

  public FindKeyMethodImpl(Args args) {
    handler = args.handler;
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

    ReturnValue rv = new ReturnValue();
    
    rv.key = "-----BEGIN PUBLIC KEY-----\n";
    String keyBase64 = DatatypeConverter.printBase64Binary(pk.getEncoded());
    while(keyBase64.length() > 0) {
      try {
        rv.key += keyBase64.substring(0, 64) + "\n";
        keyBase64 = keyBase64.substring(64);
      } catch (IndexOutOfBoundsException e) {
        rv.key += keyBase64 + "\n";
        break;
      }
    }
    rv.key += "-----END PUBLIC KEY-----\n";
    return ResponseMessage.OKMessage(gson.toJson(rv));
  }
}
