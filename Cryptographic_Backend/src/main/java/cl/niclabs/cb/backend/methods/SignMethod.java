package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;

public interface SignMethod extends Method {

  public static class ReturnValue {
      public final String sessionHandler;
      public final String signedData;

      public ReturnValue(String sessionHandler, String signedData) {
          this.sessionHandler = sessionHandler;
          this.signedData = signedData;
      }
  }

  public static class Args {
      public final String sessionHandler;
      public final String data;

      public Args(String sessionHandler, String data) {
          this.sessionHandler = sessionHandler;
          this.data = data;
      }
  }

}
