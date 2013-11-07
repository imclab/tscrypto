package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;

public interface SeedRandomMethod extends Method {
    static public class Args {
        public final String sessionHandler;
        public final String seed;

        public Args(String sessionHandler, String seed) {
            this.sessionHandler = sessionHandler;
            this.seed = seed;
        }
    }
}
