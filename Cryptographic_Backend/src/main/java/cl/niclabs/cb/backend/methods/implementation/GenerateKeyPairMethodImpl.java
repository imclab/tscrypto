package cl.niclabs.cb.backend.methods.implementation;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.GenerateKeyPairMethod;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;

class GenerateKeyPairMethodImpl extends GenerateKeyPairMethod {
    private final KeyStorage ks;

    public GenerateKeyPairMethodImpl(Args args) {
        super(args);
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
