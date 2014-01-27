package cl.niclabs.tscrypto.common.algorithms.signer;

import cl.niclabs.tscrypto.common.algorithms.JniSignWrapper;
import cl.niclabs.tscrypto.common.datatypes.KeyShareInfo;
import cl.niclabs.tscrypto.common.datatypes.SignatureShare;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class PlayerSignerJniImpl extends AbstractPlayerSigner {
    /**
     * Player signer in charge of a keyShare
     *
     * @param key key used by this player
     * @param id  player id
     */
   public PlayerSignerJniImpl (KeyShareInfo key, int id){
       super(key, id);
   }

    protected SignatureShare sign(BigInteger document, BigInteger r)throws NoSuchAlgorithmException{
        long initTime = System.currentTimeMillis();
        TSLogger.node.debug("Estoy usando JNI");
        final BigInteger groupVerifier = publicKey.getGroupVerifier();
        final BigInteger shareVerifier = publicKey.getShareVerifier(id);
        final BigInteger n = publicKey.getModulus();
        final BigInteger x = document.mod(n);

        final BigInteger x_tilde = JniSignWrapper.modPow(x, FOUR.multiply(delta), n);
        final BigInteger xi = JniSignWrapper.modPow(x, TWO.multiply(delta).multiply(secretShare), n);
        final BigInteger xi2n = JniSignWrapper.modPow(xi, TWO, n);
        final BigInteger v_prime = JniSignWrapper.modPow(groupVerifier, r, n);
        final BigInteger x_prime = JniSignWrapper.modPow(x_tilde, r, n);

        final BigInteger c;
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
        final BigInteger z = (c.multiply(secretShare)).add(r);

        final BigInteger signature = xi;

        long endTime = System.currentTimeMillis();
        TSLogger.node.info("Took " + (endTime - initTime) + "ms to complete the signature (1 call to JNI).");

        return new SignatureShare(signature, c, z);
    }
}
