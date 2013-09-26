package cb.backend.methods;

import cb.backend.Method;

public abstract class FindKeyMethod implements Method {

  public static class ReturnValue {
    public final String key;

      public ReturnValue(String key) {
          this.key = key;
      }
  }

  public static class Args {
    public long handler;
  }
  protected final long handler;

  public FindKeyMethod(Args args) {
    handler = args.handler;
  }

}
