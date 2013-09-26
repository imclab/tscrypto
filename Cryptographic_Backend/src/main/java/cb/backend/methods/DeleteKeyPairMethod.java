package cb.backend.methods;

import cb.backend.Method;

public abstract class DeleteKeyPairMethod implements Method {
    public static class Args {
        final long handler;

        public Args(long handler) {
            this.handler = handler;
        }
    }

    protected final long handler;

    public DeleteKeyPairMethod(Args args) {
        this.handler = args.handler;
    }

}
