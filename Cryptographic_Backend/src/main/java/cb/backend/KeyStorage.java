package cb.backend;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/* La idea es que estas interfaces sean implementadas como un Singleton :) */
public interface KeyStorage {
	public long storeKeyPair(String keyType, int keySize, KeyPair pair);
	public PrivateKey getPrivateKey(long handler);
	public PublicKey getPublicKey(long handler);
	public String getAttribute(String attr, long handler) throws Exception;
    public void deleteKeyPair(long handler) throws Exception;
}
