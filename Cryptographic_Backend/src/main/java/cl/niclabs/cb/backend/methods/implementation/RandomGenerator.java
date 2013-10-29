package cl.niclabs.cb.backend.methods.implementation;

import java.security.SecureRandom;

// Singleton
public class RandomGenerator {
    private static RandomGenerator INSTANCE = null;

    public static RandomGenerator getIntance() {
        if(INSTANCE == null) {
            synchronized (RandomGenerator.class) {
                if(INSTANCE == null) {
                    INSTANCE = new RandomGenerator();
                }
            }
        }

        return INSTANCE;
    }

    private SecureRandom secureRandom;

    private RandomGenerator() {
        this.secureRandom = new SecureRandom();
    }

    public void setSeed(byte []seed) {
        secureRandom.setSeed(seed);
    }

    public byte[] generateRandom(int length) {
        return secureRandom.generateSeed(length);
    }
}
