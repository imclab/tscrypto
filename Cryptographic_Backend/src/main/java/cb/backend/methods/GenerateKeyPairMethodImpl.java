package cb.backend.methods;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import cb.backend.KeyStorage;
import cb.backend.MapKeyStorage;
import cb.backend.Method;
import cb.backend.ResponseMessage;
import com.google.gson.Gson;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;

public class GenerateKeyPairMethodImpl implements Method {

  public static class ReturnValue {
    public long handler;
  }

  public static class Args {
    public String keyType;
    public int keySize;
    public String publicExponent;
  }
  private String keyType;
  private int keySize;
  private String publicExponent;
  private KeyStorage ks;

  public GenerateKeyPairMethodImpl(String keyType, int keySize) {
    this.keyType = keyType;
    this.keySize = keySize;
    this.publicExponent = "65537";

    this.ks = MapKeyStorage.getInstance();
  }

  public GenerateKeyPairMethodImpl(Args args) {
    this.keyType = args.keyType;
    this.keySize = args.keySize;
    this.publicExponent = args.publicExponent;

    this.ks = MapKeyStorage.getInstance();
  }

  @Override
  public ResponseMessage execute() {
    /* Forma de usar gson bknmente. */
    ReturnValue rv = new ReturnValue();

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
      rv.handler = handler;

      ResponseMessage rm = ResponseMessage.OKMessage(gson.toJson(rv));
      return rm;
    } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
      return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
    }

  }

  public static void main(String[] args) {
    GenerateKeyPairMethodImpl gkpm = new GenerateKeyPairMethodImpl("RSA", 1024);
    ResponseMessage rm = gkpm.execute();
    System.out.println(new Gson().toJson(rm));
  }
}
