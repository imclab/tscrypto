package cl.inria.tscrypto.node;

import java.math.BigInteger;
import java.util.Hashtable;

import cl.inria.tscrypto.common.algorithms.PlayerSigner;
import cl.inria.tscrypto.common.algorithms.PlayerSignerImpl;
import cl.inria.tscrypto.common.datatypes.KeyInfo;
import cl.inria.tscrypto.common.datatypes.KeyShareInfo;
import cl.inria.tscrypto.common.datatypes.TSPublicKey;

public class KeyShareManager {

    private Hashtable<String, KeyShareInfo> keys = new Hashtable<>();
	
	public void addKey(String label, KeyShareInfo key) {
		keys.put(label, key);
	}

    public void addKey(KeyShareInfo key) {
        addKey(key.getKeyMetaInfo().getAlias(), key);
    }
	
	public void removeKey(String label) {
		if (keys.contains(label)) {
            keys.remove(label);
        }
	}

	public TSPublicKey getPublicKey(String label) {
		return keys.get(label).getPublicKey();
	}

	public BigInteger getKeyShare(String label, int id) {
		return keys.get(label).getShare();
	}

    public KeyShareInfo getKeyShareInfo(String label) {
        return keys.get(label);
    }
}
