package cl.niclabs.cb.common;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface KeyOperations {
    public static class GenerationResult {
        public final String keyHandler;
        public final String modulus;
        public final String publicExponent;

        public GenerationResult(String keyHandler, String modulus, String publicExponent) {
            this.keyHandler = keyHandler;
            this.modulus = modulus;
            this.publicExponent = publicExponent;
        }
    }

    public GenerationResult generateKeys(String keyType, int keySize, String publicExponent) throws Exception;
    public void deleteKeys(String keyHandler) throws Exception;
    public String findKey(String keyHandler) throws InvalidKeySpecException, NoSuchAlgorithmException;
}
