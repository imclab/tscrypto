package cl.niclabs.tscrypto.common.algorithms;

import java.math.BigInteger;
class ModPowWrapper {
    static {
        System.out.println("Loading library...");
        System.loadLibrary("modPowWrapper");
        System.out.println("Library loaded...");
    }
    private static native String mPow(String num, String expo, String mod);
    private final static int numTest = 100;

    public static BigInteger modPowWrapper(BigInteger num, BigInteger exp, BigInteger mod ){
        String aux = mPow(num.toString(), exp.toString(), mod.toString());
        return new BigInteger(aux.trim(),16);
    }
}