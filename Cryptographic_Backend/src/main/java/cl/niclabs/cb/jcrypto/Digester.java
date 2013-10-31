package cl.niclabs.cb.jcrypto;

import java.security.NoSuchAlgorithmException;

public interface Digester {
    void digestInit(String mechanism) throws NoSuchAlgorithmException;

    byte[] digest(byte[] data) throws Exception;
}
