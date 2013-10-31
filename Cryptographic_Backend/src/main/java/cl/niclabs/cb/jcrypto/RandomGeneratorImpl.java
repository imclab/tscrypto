package cl.niclabs.cb.jcrypto;

import java.security.SecureRandom;

// Singleton
public class RandomGeneratorImpl implements RandomGenerator {
    private static RandomGenerator instance = null;

    public static RandomGenerator getIntance() {
        if(instance == null) {
            synchronized (RandomGeneratorImpl.class) {
                if(instance == null) {
                    instance = new RandomGeneratorImpl();
                }
            }
        }

        return instance;
    }

    private SecureRandom secureRandom;

    private RandomGeneratorImpl() {
        this.secureRandom = new SecureRandom();
    }

    @Override
    public void setSeed(byte[] seed) {
        secureRandom.setSeed(seed);
    }

    @Override
    public byte[] generateRandom(int length) {
        return secureRandom.generateSeed(length);
    }
}
