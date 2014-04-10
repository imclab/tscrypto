package cl.niclabs.cb.jcrypto;

import cl.niclabs.cb.common.KeyOperations;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;

public class KeyOperationsImpl implements KeyOperations {
    private final KeyStorage ks;

    public KeyOperationsImpl(KeyStorage ks) {
        this.ks = ks;
    }

    @Override
    public GenerationResult generateKeys(String keyType, int keySize, String publicExponentB64) throws Exception {
        byte[] publicExponent = DatatypeConverter.parseBase64Binary(publicExponentB64);
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(keyType);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        AlgorithmParameterSpec rsaParams =
                new RSAKeyGenParameterSpec(keySize, new BigInteger(publicExponent));

        kpg.initialize(rsaParams, random);

        KeyPair pair = kpg.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) pair.getPublic();
        String handler = ks.storeKeyPair(keyType, keySize, pair);

        String modulus = DatatypeConverter.printBase64Binary(publicKey.getModulus().toByteArray());
        publicExponentB64 = DatatypeConverter.printBase64Binary(publicKey.getPublicExponent().toByteArray());

        return new GenerationResult(handler, modulus, publicExponentB64);
    }

    @Override
    public void deleteKeys(String keyHandler) throws Exception {
            ks.deleteKeyPair(keyHandler);
    }

    @Override
    public String findKey(String handler) {
        KeyStorage ks = MapKeyStorage.getInstance();
        PublicKey pk;
        pk = ks.getPublicKey(handler);

        String key = "-----BEGIN PUBLIC KEY-----\n";
        String keyBase64 = DatatypeConverter.printBase64Binary(pk.getEncoded());
        while(keyBase64.length() > 0) {
            try {
                key += keyBase64.substring(0, 64) + "\n";
                keyBase64 = keyBase64.substring(64);
            } catch (IndexOutOfBoundsException e) {
                key += keyBase64 + "\n";
                break;
            }
        }
        key += "-----END PUBLIC KEY-----\n";

        return key;
    }
}
