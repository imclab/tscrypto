package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;

public interface DigestMethod extends Method {
    public static class Args {
        public final String sessionHandler;
        public final String data;

        public Args(String sessionHandler, String data) {
            this.sessionHandler = sessionHandler;
            this.data = data;
        }

    }
    public static class ReturnValue {

        public final String digest;
        public ReturnValue(String digest) {
            this.digest = digest;
        }

    }
}
