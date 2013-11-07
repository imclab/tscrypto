package cl.niclabs.cb.backend;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface Signer {
    void init (String algorithm, String keyHandler)
            throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException;

    byte[] sign(byte[] data) throws Exception;
}
