package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;

public interface GenerateRandomMethod extends Method {
    public static class Args {
        public final int length;

        public Args(int length) {
            this.length = length;
        }
    }

    public static class ReturnValue {
        public final String data;

        public ReturnValue(String data) {
            this.data = data;
        }
    }
}