package cb.backend;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Random;
import javax.xml.bind.DatatypeConverter;

public class MapKeyStorage implements KeyStorage {

  final static private KeyStorage instance = new MapKeyStorage();
  HashMap<Integer, String[]> store;

  public static KeyStorage getInstance() {
    return instance;
  }

  private MapKeyStorage() {
    store = new HashMap<>();
  }

  @Override
  public int storeKeyPair(String keyType, int keySize, KeyPair pair) {

    X509EncodedKeySpec publicSpec =
            new X509EncodedKeySpec(pair.getPublic().getEncoded());
    PKCS8EncodedKeySpec privateSpec =
            new PKCS8EncodedKeySpec(pair.getPrivate().getEncoded());

    String[] vals = {
      keyType,
      Integer.toString(keySize),
      DatatypeConverter.printBase64Binary(publicSpec.getEncoded()),
      DatatypeConverter.printBase64Binary(privateSpec.getEncoded())
    };

    Random random = new Random();
    Integer handle;
    do {
      handle = random.nextInt();
    } while (store.containsKey(handle));

    store.put(handle, vals);

    return handle.intValue();
  }

  @Override
  public PrivateKey getPrivateKey(int _handler) {
    Integer handler = _handler;
    byte[] byteKey = DatatypeConverter.parseBase64Binary(store.get(handler)[3]);
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(byteKey);

    try {
      KeyFactory kf = KeyFactory.getInstance(store.get(handler)[0]);
      return kf.generatePrivate(spec);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      return null; // No debería llegar acá nunca.
    }

  }

  @Override
  public PublicKey getPublicKey(int _handler) {
    Integer handler = _handler;
    byte[] byteKey = DatatypeConverter.parseBase64Binary(store.get(handler)[2]);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(byteKey);

    try {
      KeyFactory kf = KeyFactory.getInstance(store.get(handler)[0]);
      return kf.generatePublic(spec);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      return null;
    }

  }

  @Override
  public String getAttribute(String attr, int handler) throws Exception {
    String[] values = store.get(new Integer(handler));
    switch (attr) {
      case "keyType":
        return values[0];
      case "keySize":
        return values[1];
      default:
        throw new Exception("Atributo no valido.");
    }
  }
}
