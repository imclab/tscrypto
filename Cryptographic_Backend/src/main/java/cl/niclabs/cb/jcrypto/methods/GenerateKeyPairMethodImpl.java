package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.GenerateKeyPairMethod;
import cl.niclabs.cb.jcrypto.KeyStorage;
import cl.niclabs.cb.jcrypto.MapKeyStorage;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;

class GenerateKeyPairMethodImpl implements GenerateKeyPairMethod {
    protected final String keyType;
    protected final int keySize;
    protected final String publicExponent;
    private final KeyStorage ks;

    public GenerateKeyPairMethodImpl(Args args) {
        keyType = args.keyType;
        keySize = args.keySize;
        publicExponent = args.publicExponent;
        ks = MapKeyStorage.getInstance();
    }

    @Override
    public ResponseMessage execute() {

      try {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(keyType);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        AlgorithmParameterSpec rsaParams =
                new RSAKeyGenParameterSpec(keySize, new BigInteger(publicExponent));

        kpg.initialize(rsaParams, random);

        KeyPair pair = kpg.generateKeyPair();
        String handler = ks.storeKeyPair(keyType, keySize, pair);

        return ResponseMessage.OKMessage(new ReturnValue(handler));
      } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
        return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
      }

    }
}
