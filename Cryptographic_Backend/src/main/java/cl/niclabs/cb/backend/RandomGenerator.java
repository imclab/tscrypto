package cl.niclabs.cb.backend;

public interface RandomGenerator {
    void setSeed(byte[] seed);

    byte[] generateRandom(int length);
}
