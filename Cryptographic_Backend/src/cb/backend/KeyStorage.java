package cb.backend;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/* La idea es que estas interfaces sean implementadas como un Singleton :) */
public interface KeyStorage {
	abstract public long storeKeyPair(String keyType, int keySize, KeyPair pair);
	abstract public PrivateKey getPrivateKey(long handler);
	abstract public PublicKey getPublicKey(long handler);
	abstract public String getAttribute(String attr, long handler) throws Exception;
}
