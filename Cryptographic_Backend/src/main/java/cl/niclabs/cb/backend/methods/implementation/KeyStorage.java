package cl.niclabs.cb.backend.methods.implementation;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface KeyStorage {
	public String storeKeyPair(String keyType, int keySize, KeyPair pair);
	public PrivateKey getPrivateKey(String handler);
	public PublicKey getPublicKey(String handler);
	public String getAttribute(String attr, String handler) throws Exception;
    public void deleteKeyPair(String handler) throws Exception;
}
