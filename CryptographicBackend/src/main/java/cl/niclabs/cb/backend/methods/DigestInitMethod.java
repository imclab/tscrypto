package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;

public interface DigestInitMethod extends Method {
    public static class Args {
        public final String sessionHandler;
        public final String mechanism;

        public Args(String sessionHandler, String mechanism) {
            this.mechanism = mechanism;
            this.sessionHandler = sessionHandler;
        }
    }
}
