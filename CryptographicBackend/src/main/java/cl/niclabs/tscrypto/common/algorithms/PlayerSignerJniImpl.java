package cl.niclabs.tscrypto.common.algorithms;

import cl.niclabs.tscrypto.common.datatypes.KeyShareInfo;
import cl.niclabs.tscrypto.common.datatypes.SignatureShare;
import cl.niclabs.tscrypto.common.datatypes.TSPublicKey;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.common.utils.ThreshUtil;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class PlayerSignerJniImpl implements PlayerSigner {
    public final static BigInteger TWO = ThreshUtil.TWO;
    public final static BigInteger FOUR = ThreshUtil.FOUR;

    /** player id */
    private int id;

    /** l! the factorial of the number of players */
    private BigInteger delta;

    /** The share hold to this player */
    private BigInteger secretShare;

    /** Public key information (includes verifiers) */
    private TSPublicKey publicKey;

    private static SecureRandom random;
    private MessageDigest md;

    /** lock to be used with Message Digest since it is not thread-safe */
    private static Object lockMd = new Object();

    static {
        final byte[] randSeed = new byte[20];
        (new Random()).nextBytes(randSeed);
        random = new SecureRandom(randSeed);
    }

    /**
     * Player signer in charge of a keyShare
     *
     * @param key key used by this player
     * @param id  player id
     */
    public PlayerSignerJniImpl(KeyShareInfo key, int id) {
        this.secretShare = key.getShare();
        this.publicKey = key.getPublicKey();
        this.id = id;

        this.delta = ThreshUtil.factorial(key.getKeyMetaInfo().getL());
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public SignatureShare sign(BigInteger document) throws NoSuchAlgorithmException {
        final int randbits = publicKey.getModulus().bitLength() + 3 * ThreshUtil.L1;
        final BigInteger r = (new BigInteger(randbits, random));

        return jniSign(document, r);
    }

    private SignatureShare jniSign(BigInteger document, BigInteger r)throws NoSuchAlgorithmException{
        long initTime = System.currentTimeMillis();
        TSLogger.node.debug("Estoy usando JNI");
        BigInteger groupVerifier = publicKey.getGroupVerifier();
        BigInteger shareVerifier = publicKey.getShareVerifier(id);
        final BigInteger n = publicKey.getModulus();
        final BigInteger x = document.mod(n);

        BigInteger x_tilde = JniSignWrapper.modPow(x, FOUR.multiply(delta), n);
        BigInteger xi = JniSignWrapper.modPow(x, TWO.multiply(delta).multiply(secretShare), n);
        BigInteger xi2n = JniSignWrapper.modPow(xi, TWO, n);
        BigInteger v_prime = JniSignWrapper.modPow(groupVerifier, r, n);
        BigInteger x_prime = JniSignWrapper.modPow(x_tilde, r, n);

        BigInteger [] res = JniSignWrapper.signWrapper(groupVerifier, shareVerifier, n, x, r, delta, secretShare);

        BigInteger c, z;
        synchronized (lockMd){
            md = MessageDigest.getInstance("SHA");
            md.reset();
            md.update(groupVerifier.mod(n).toByteArray());
            md.update(x_tilde.toByteArray());
            md.update(shareVerifier.mod(n).toByteArray());
            md.update(xi2n.toByteArray());
            md.update(v_prime.toByteArray());
            md.update(x_prime.toByteArray());
            c = new BigInteger(md.digest()).mod(n);
        }
        z = (c.multiply(secretShare)).add(r);
        BigInteger signature = new BigInteger(res[0].toByteArray()); // TODO: clean this.

        long endTime = System.currentTimeMillis();
        TSLogger.node.info("Took " + (endTime - initTime) + "ms to complete the signature (1 call to JNI).");

        return new SignatureShare(signature, c, z);
    }
}
