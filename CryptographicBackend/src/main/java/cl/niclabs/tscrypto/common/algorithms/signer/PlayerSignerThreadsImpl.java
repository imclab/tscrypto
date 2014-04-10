package cl.niclabs.tscrypto.common.algorithms.signer;

import cl.niclabs.tscrypto.common.algorithms.JniSignWrapper;
import cl.niclabs.tscrypto.common.datatypes.KeyShareInfo;
import cl.niclabs.tscrypto.common.datatypes.SignatureShare;
import cl.niclabs.tscrypto.common.utils.TSLogger;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.*;


public class PlayerSignerThreadsImpl extends AbstractPlayerSigner {

    final int threads;
    /**
     * Player signer in charge of a keyShare
     *
     * @param key key used by this player
     * @param id  player id
     */
   public PlayerSignerThreadsImpl(KeyShareInfo key, int id, int threads){
       super(key, id);

       this.threads = threads;
   }

    protected SignatureShare sign(final BigInteger document, final BigInteger r)throws NoSuchAlgorithmException{
        long initTime = System.currentTimeMillis();
        TSLogger.node.debug("Estoy usando JNI");
        final BigInteger groupVerifier = publicKey.getGroupVerifier();
        final BigInteger shareVerifier = publicKey.getShareVerifier(id);
        final BigInteger n = publicKey.getModulus();
        final BigInteger x = document.mod(n);

        ExecutorService executor = Executors.newFixedThreadPool(threads);

        final Future<BigInteger> x_tilde = executor.submit(new Callable<BigInteger>() {
            @Override
            public BigInteger call() throws Exception {
                return x.modPow(FOUR.multiply(delta),n);
            }
        });
        final Future<BigInteger> xi = executor.submit(new Callable<BigInteger>() {
            @Override
            public BigInteger call() throws Exception {
                return x.modPow(TWO.multiply(delta).multiply(secretShare),n);
            }
        });
        final Future<BigInteger> xi2n = executor.submit(new Callable<BigInteger>() {
            @Override
            public BigInteger call() throws Exception {
                return xi.get().modPow(TWO,n);
            }
        });
        final Future<BigInteger> v_prime = executor.submit(new Callable<BigInteger>() {
            @Override
            public BigInteger call() throws Exception {
                return groupVerifier.modPow(r,n);
            }
        });
        final Future<BigInteger> x_prime = executor.submit(new Callable<BigInteger>() {
            @Override
            public BigInteger call() throws Exception {
                return x_tilde.get().modPow(r,n);
            }
        });

        try {
        final BigInteger c;
        synchronized (lockMd){
            md = MessageDigest.getInstance("SHA");
            md.reset();
            md.update(groupVerifier.mod(n).toByteArray());
            md.update(x_tilde.get().toByteArray());
            md.update(shareVerifier.mod(n).toByteArray());
            md.update(xi2n.get().toByteArray());
            md.update(v_prime.get().toByteArray());
            md.update(x_prime.get().toByteArray());
            c = new BigInteger(md.digest()).mod(n);
        }
        final BigInteger z = (c.multiply(secretShare)).add(r);

        final BigInteger signature = xi.get();


        long endTime = System.currentTimeMillis();
        TSLogger.node.info("Took " + (endTime - initTime) + "ms to complete the signature (1 call to JNI).");

        return new SignatureShare(signature, c, z);

        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }
}
