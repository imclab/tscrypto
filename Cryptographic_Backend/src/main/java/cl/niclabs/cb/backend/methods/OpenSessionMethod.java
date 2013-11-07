package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;

public interface OpenSessionMethod extends Method {
    public static class ReturnValue {
        public final String sessionHandler;

        public ReturnValue(String sessionHandler) {
            this.sessionHandler = sessionHandler;
        }
    }

}
