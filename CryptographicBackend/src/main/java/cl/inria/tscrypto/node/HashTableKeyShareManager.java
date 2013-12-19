package cl.inria.tscrypto.node;

import java.util.Hashtable;

import cl.inria.tscrypto.common.datatypes.KeyShareInfo;
import cl.inria.tscrypto.common.datatypes.TSPublicKey;

public class HashTableKeyShareManager implements KeyShareManager {

    private Hashtable<String, KeyShareInfo> keys = new Hashtable<>();

    public HashTableKeyShareManager() {}

    @Override
    public void addKey(String label, KeyShareInfo key) {
		keys.put(label, key);
	}
	
	@Override
    public void removeKey(String label) {
		if (keys.contains(label)) {
            keys.remove(label);
        }
	}

	@Override
    public TSPublicKey getPublicKey(String label) {
		return keys.get(label).getPublicKey();
	}

    @Override
    public KeyShareInfo getKeyShareInfo(String label) {
        return keys.get(label);
    }
}
