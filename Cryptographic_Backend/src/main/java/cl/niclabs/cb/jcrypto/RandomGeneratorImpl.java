package cl.niclabs.cb.jcrypto;

import java.security.SecureRandom;

public class RandomGeneratorImpl implements RandomGenerator {
    private SecureRandom secureRandom;

    RandomGeneratorImpl() {
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
