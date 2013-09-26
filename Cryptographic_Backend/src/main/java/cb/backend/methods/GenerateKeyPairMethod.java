package cb.backend.methods;

import cb.backend.KeyStorage;
import cb.backend.MapKeyStorage;
import cb.backend.Method;

public abstract class GenerateKeyPairMethod implements Method {

  public static class ReturnValue {
    public final long handler;

      public ReturnValue(long handler) {
          this.handler = handler;
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

  protected final String keyType;
  protected final int keySize;
  protected final String publicExponent;
  protected final KeyStorage ks;

  public GenerateKeyPairMethod(Args args) {
    this.keyType = args.keyType;
    this.keySize = args.keySize;
    this.publicExponent = args.publicExponent;

    this.ks = MapKeyStorage.getInstance();
  }

}
