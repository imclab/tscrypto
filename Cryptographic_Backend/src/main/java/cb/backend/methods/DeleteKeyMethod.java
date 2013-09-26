package cb.backend.methods;

import cb.backend.Method;

public abstract class DeleteKeyMethod implements Method {
    public static class Args {
        final long handler;

        public Args(long handler) {
            this.handler = handler;
        }
    }

    protected final long handler;

    public DeleteKeyMethod(Args args) {
        this.handler = args.handler;
    }

}
