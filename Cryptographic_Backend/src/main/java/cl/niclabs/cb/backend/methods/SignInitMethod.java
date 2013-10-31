package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.*;

public interface SignInitMethod extends Method {
	public static class Args {
		public final String mechanism;
		public final String handler;

        public Args(String mechanism, String handler) {
            this.mechanism = mechanism;
            this.handler = handler;
        }
    }
}
