package cl.niclabs.cb.jcrypto;

public interface RandomGenerator {
    void setSeed(byte[] seed);

    byte[] generateRandom(int length);
}
