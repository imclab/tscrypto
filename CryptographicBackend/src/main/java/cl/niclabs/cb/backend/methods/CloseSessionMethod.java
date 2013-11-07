package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;

public interface CloseSessionMethod extends Method {
    public static class Args {
        public final String sessionHandler;

        public Args(String sessionHandler) {
            this.sessionHandler = sessionHandler;
        }
    }
}
