package cl.niclabs.tscrypto.common.algorithms;

import java.math.BigInteger;
public class JniSignWrapper {
    static {
        System.out.println("Loading library...");
        System.loadLibrary("jniSignWrapper");
        System.out.println("Library loaded...");
    }

    private static native byte[] modPow(byte[] num, byte[] expo, byte[] mod);
    private static native String[] sign(String groupVerifier, String shareVerifier,
                                        String n, String x, String r, String delta,
                                        String secretShare);

    public static BigInteger modPow(BigInteger num, BigInteger exp, BigInteger mod){
        // The sign is always positive.
       return new BigInteger(1, modPow(num.toByteArray(), exp.toByteArray(), mod.toByteArray()));
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