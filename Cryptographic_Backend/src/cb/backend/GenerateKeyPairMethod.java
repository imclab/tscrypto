package cb.backend;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.google.gson.Gson;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;

public class GenerateKeyPairMethod implements Method {

  public static class ReturnValue {
    public int handler;
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

  public GenerateKeyPairMethod(String keyType, int keySize) {
    this.keyType = keyType;
    this.keySize = keySize;
    this.publicExponent = "65537";

    this.ks = MapKeyStorage.getInstance();
  }

  public GenerateKeyPairMethod(Args args) {
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
      int handler = ks.storeKeyPair(keyType, keySize, pair);

      // Defino el return value, para ser luego serializado en json.
      rv.handler = handler;

      ResponseMessage rm = ResponseMessage.OKMessage(gson.toJson(rv));
      return rm;
    } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
      return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
    }

  }

  public static void main(String[] args) {
    GenerateKeyPairMethod gkpm = new GenerateKeyPairMethod("RSA", 1024);
    ResponseMessage rm = gkpm.execute();
    System.out.println(rm);
  }
}
