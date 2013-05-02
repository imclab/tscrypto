package cb;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/* La idea es que estas interfaces sean implementadas como un Singleton :) */
public interface KeyStorage {
	abstract public int storeKeyPair(String id, String label, String keyType, int keySize, KeyPair pair);
	abstract public PrivateKey getPrivateKey(int handler);
	abstract public PublicKey getPublicKey(int handler);
	abstract public int findHandler(String id);
}
