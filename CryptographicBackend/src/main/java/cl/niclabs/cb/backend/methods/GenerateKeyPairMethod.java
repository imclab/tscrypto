package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;

public interface GenerateKeyPairMethod extends Method {

  public static class ReturnValue {
    public final String keyHandler;

      public ReturnValue(String keyHandler) {
          this.keyHandler = keyHandler;
      }
  }

  public static class Args {
    public final String keyType;
    public final int keySize;
    public final String publicExponent;

      public Args(String keyType, int keySize, String publicExponent) {
          this.keyType = keyType;
          this.keySize = keySize;
          this.publicExponent = publicExponent;
      }
  }

}
