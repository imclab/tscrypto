package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;
import javax.xml.bind.DatatypeConverter;

public abstract class SignMethod implements Method {

  public static class ReturnValue {
      public final String signedData;

      public ReturnValue(String signedData) {
          this.signedData = signedData;
      }
  }

  public static class Args {
    public final String data;

      public Args(String data) {
          this.data = data;
      }
  }

  protected byte[] data;

  public SignMethod(Args args) {
    data = DatatypeConverter.parseBase64Binary(args.data);
  }

}
