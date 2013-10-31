package cl.niclabs.cb.jcrypto;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public interface Signer {
    void init (String algorithm, PrivateKey key)
            throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException;

    byte[] sign(byte[] data) throws Exception;
}
