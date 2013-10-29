package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;

import javax.xml.bind.DatatypeConverter;

public abstract class SeedRandomMethod implements Method {
    static public class Args {
        public final String seed;

        public Args(String seed) {
            this.seed = seed;
        }
    }

    protected final byte[] seed;

    public SeedRandomMethod(Args args) {
        seed = DatatypeConverter.parseBase64Binary(args.seed);
    }
}
