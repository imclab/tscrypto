package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;

public interface FindKeyMethod extends Method {

  public static class ReturnValue {
    public final String key;

      public ReturnValue(String key) {
          this.key = key;
      }
  }

  public static class Args {
    public String handler;
  }

}
