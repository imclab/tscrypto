package cb.backend;

import com.google.gson.Gson;
import java.security.PublicKey;

public class FindKeyMethod implements Method {

  public static class ReturnValue {

    public String key;
  }

  public static class Args {
    public int handler;
  }
  private int handler;

  public FindKeyMethod(Args args) {
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
    rv.key = Signer.bytesToHex(pk.getEncoded());
    return ResponseMessage.OKMessage(gson.toJson(rv));
  }
}
