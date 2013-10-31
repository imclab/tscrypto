package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;

import javax.xml.bind.DatatypeConverter;

public interface DigestMethod extends Method {
    public static class Args {

        public final String data;
        public Args(String data) {
            this.data = data;
        }

    }
    public static class ReturnValue {

        public final String digest;
        public ReturnValue(String digest) {
            this.digest = digest;
        }

    }
}
