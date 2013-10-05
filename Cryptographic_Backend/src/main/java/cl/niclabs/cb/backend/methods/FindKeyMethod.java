package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;

public abstract class FindKeyMethod implements Method {

  public static class ReturnValue {
    public final String key;

      public ReturnValue(String key) {
          this.key = key;
      }
  }

  public static class Args {
    public String handler;
  }
  protected final String handler;

  public FindKeyMethod(Args args) {
    handler = args.handler;
  }

}
