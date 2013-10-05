package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.*;

public abstract class SignInitMethod implements Method {
	public static class Args {
		public final String mechanism;
		public final String handler;

        public Args(String mechanism, String handler) {
            this.mechanism = mechanism;
            this.handler = handler;
        }
    }
	protected final String mechanism;
	protected final String privateKeyHandler;
	
	public SignInitMethod(Args args) {
		this.mechanism = args.mechanism;
		this.privateKeyHandler = args.handler;
	}
}
