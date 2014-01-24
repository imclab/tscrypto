package cl.niclabs.tscrypto.common.algorithms;

import java.math.BigInteger;
class JniSignWrapper {
    static {
        System.out.println("Loading library...");
        System.loadLibrary("jniSignWrapper");
        System.out.println("Library loaded...");
    }
    private static native String mPow(String num, String expo, String mod);
    private static native String[] sign(String groupVerifier, String shareVerifier,
                                        String n, String x, String r, String delta,
                                        String secretShare);
    private final static int numTest = 100;

    public static BigInteger modPowWrapper(BigInteger num, BigInteger exp, BigInteger mod ){
        String aux = mPow(num.toString(), exp.toString(), mod.toString());
        return new BigInteger(aux.trim(),16);
    }
    public static BigInteger[] signWrapper(BigInteger groupVerifier, BigInteger shareVerifier,
                                           BigInteger n, BigInteger x, BigInteger r, BigInteger delta,
                                           BigInteger secretShare){
        String result[] = sign(groupVerifier.toString(), shareVerifier.toString(),
                n.toString(), x.toString(), r.toString(), delta.toString(), secretShare.toString());
        return new BigInteger[]
                {new BigInteger(result[0].trim(), 16), new BigInteger(result[1].trim(),16),new BigInteger(result[2].trim(),16),
                        new BigInteger(result[3].trim(),16),new BigInteger(result[4].trim(),16)};
    }
}