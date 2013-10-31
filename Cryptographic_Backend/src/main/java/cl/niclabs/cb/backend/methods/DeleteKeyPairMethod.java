package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;

public interface DeleteKeyPairMethod extends Method {
    public static class Args {
        public final String handler;

        public Args(String handler) {
            this.handler = handler;
        }
    }
}
