package cb.backend.methods;

import cb.backend.*;

public abstract class SignInitMethod implements Method {
	public static class Args {
		public final String mechanism;
		public final long handler;

        public Args(String mechanism, long handler) {
            this.mechanism = mechanism;
            this.handler = handler;
        }
    }
	protected final String mechanism;
	protected final long privateKeyHandler;
	
	public SignInitMethod(Args args) {
		this.mechanism = args.mechanism;
		this.privateKeyHandler = args.handler;
	}
}
