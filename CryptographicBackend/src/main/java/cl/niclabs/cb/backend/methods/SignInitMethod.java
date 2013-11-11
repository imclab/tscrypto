package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;

public interface SignInitMethod extends Method {
	public static class Args {
        public final String sessionHandler;
		public final String mechanism;
		public final String keyHandler;

        public Args(String sessionHandler, String mechanism, String keyHandler) {
            this.sessionHandler = sessionHandler;
            this.mechanism = mechanism;
            this.keyHandler = keyHandler;
        }
    }
}
