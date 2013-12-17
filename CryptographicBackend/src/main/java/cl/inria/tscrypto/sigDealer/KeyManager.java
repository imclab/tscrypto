package cl.inria.tscrypto.sigDealer;

import cl.inria.tscrypto.common.datatypes.KeyInfo;
import cl.inria.tscrypto.common.datatypes.TSPublicKey;

import java.math.BigInteger;
import java.util.Hashtable;

public class KeyManager {
    private Hashtable<String, KeyInfo> keys = new Hashtable<>();

    public void addKey(String label, KeyInfo key) {
        keys.put(label, key);
    }

    public void addKey(KeyInfo key) {
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
        return keys.get(label).getKeyShares().getSecret(id);
    }

    public KeyInfo getKeyInfo(String label) {
        return keys.get(label);
    }
}
