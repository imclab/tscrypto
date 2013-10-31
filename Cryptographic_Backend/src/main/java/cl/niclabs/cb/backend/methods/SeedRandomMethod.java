package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;

import javax.xml.bind.DatatypeConverter;

public interface SeedRandomMethod extends Method {
    static public class Args {
        public final String seed;

        public Args(String seed) {
            this.seed = seed;
        }
    }
}
