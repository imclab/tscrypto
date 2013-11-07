package cl.niclabs.cb.jcrypto;

import javax.xml.bind.DatatypeConverter;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

public class MapKeyStorage implements KeyStorage {

    static private KeyStorage instance = null;

    public static KeyStorage getInstance() {
        if (instance == null) {
            synchronized (KeyStorage.class) {
                if (instance == null) {
                    instance = new MapKeyStorage();
                }
            }
        }

        return instance;
    }

    private final Map<String, String[]> store = new Hashtable<>();

    private MapKeyStorage() {
    }

    @Override
    public String storeKeyPair(String keyType, int keySize, KeyPair pair) {

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


        String handle;
        do {
            handle = UUID.randomUUID().toString();
        } while (store.containsKey(handle));

        store.put(handle, vals);

        return handle;
    }

    @Override
    public PrivateKey getPrivateKey(String handler) {
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
    public PublicKey getPublicKey(String handler) {
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
    public String getAttribute(String attr, String handler) throws Exception {
        String[] values;
        if (!store.containsKey(handler))
            throw new Exception("Llave inexistente.");

        values = store.get(handler);
        switch (attr) {
            case "keyType":
                return values[0];
            case "keySize":
                return values[1];
            default:
                throw new Exception("Atributo no valido.");
        }
    }

    @Override
    public void deleteKeyPair(String handler) throws Exception {
        if (!store.containsKey(handler)) {
            throw new Exception("Llave no existente.") ;
        }

        store.remove(handler);
    }
}
