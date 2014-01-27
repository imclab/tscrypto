package cl.niclabs.tscrypto.common.algorithms.signer;

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

/**
 * Created by niclabs on 1/27/14.
 */
public abstract class AbstractPlayerSigner implements PlayerSigner{
    public final static BigInteger TWO = ThreshUtil.TWO;
    public final static BigInteger FOUR = ThreshUtil.FOUR;

    /** player id */
    protected int id;

    /** l! the factorial of the number of players */
    protected BigInteger delta;

    /** The share hold to this player */
    protected BigInteger secretShare;

    /** Public key information (includes verifiers) */
    protected TSPublicKey publicKey;

    protected static SecureRandom random;

    protected MessageDigest md;

    /** lock to be used with Message Digest since it is not thread-safe */
    protected static Object lockMd = new Object();

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
    public AbstractPlayerSigner(KeyShareInfo key, int id) {
        TSLogger.node.info("Creating player " + id);
        TSLogger.node.debug(key.toJson(true));
        this.secretShare = key.getShare();
        this.publicKey = key.getPublicKey();
        this.id = id;
        this.delta = ThreshUtil.factorial(key.getKeyMetaInfo().getL());
    }

    public int getId() {
        return id;
    }

    abstract protected SignatureShare sign(BigInteger document, BigInteger r) throws NoSuchAlgorithmException;

    public SignatureShare sign(BigInteger document) throws NoSuchAlgorithmException {
        final int randbits = publicKey.getModulus().bitLength() + 3 * ThreshUtil.L1;
        final BigInteger r = (new BigInteger(randbits, random));

        return sign(document, r);
    }

}
