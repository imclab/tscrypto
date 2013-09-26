package cb.backend.methods;

import cb.backend.ResponseMessage;
import com.google.gson.Gson;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;

class GenerateKeyPairMethodImpl extends GenerateKeyPairMethod {
    public GenerateKeyPairMethodImpl(Args args) {
        super(args);
    }

    @Override
    public ResponseMessage execute() {
      Gson gson = new Gson();

      try {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(keyType);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        AlgorithmParameterSpec rsaParams =
                new RSAKeyGenParameterSpec(keySize, new BigInteger(publicExponent));

        kpg.initialize(rsaParams, random);

        KeyPair pair = kpg.generateKeyPair();
        long handler = ks.storeKeyPair(keyType, keySize, pair);

        // Defino el return value, para ser luego serializado en json.
        ReturnValue rv = new ReturnValue(handler);

        return ResponseMessage.OKMessage(gson.toJson(rv));
      } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
        return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
      }

    }
}
