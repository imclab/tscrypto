package cl.niclabs.cb.backend;

public interface Session {
    String getHandler();

    Signer getSigner();

    Digester getDigester();

    RandomGenerator getRandomGenerator();

    @Override
    String toString();
}
