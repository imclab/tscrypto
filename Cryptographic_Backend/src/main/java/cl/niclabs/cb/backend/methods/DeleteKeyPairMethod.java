package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;

public abstract class DeleteKeyPairMethod implements Method {
    public static class Args {
        final String handler;

        public Args(String handler) {
            this.handler = handler;
        }
    }

    protected final String handler;

    public DeleteKeyPairMethod(Args args) {
        this.handler = args.handler;
    }

}
