package cl.inria.tscrypto.sigDealer;

import cl.inria.tscrypto.common.datatypes.KeyInfo;
import cl.inria.tscrypto.common.datatypes.KeyMetaInfo;
import cl.inria.tscrypto.common.datatypes.TSPublicKey;

import java.util.Hashtable;
import java.util.Map;

public class HashTableKeyManager implements KeyManager { //TODO: check if it's thread safe...

    static public class Pair {
        TSPublicKey publicKey;
        KeyMetaInfo keyMetaInfo;
    }

    private Map<String, Pair> map = new Hashtable<>();

    @Override
    public void addKey(KeyInfo key) {
        String label = key.getKeyMetaInfo().getAlias();
        Pair pair = new Pair();
        pair.publicKey = key.getPublicKey();
        pair.keyMetaInfo = key.getKeyMetaInfo();
        map.put(label, pair);
    }

    @Override
    public void removeKey(String label) {
        map.remove(label);
    }

    @Override
    public boolean containsKey(String label) {
        return map.containsKey(label);
    }

    @Override
    public TSPublicKey getPublicKey(String label) {
        return map.get(label).publicKey;
    }

    @Override
    public KeyMetaInfo getKeyMetaInfo(String label) {
        return map.get(label).keyMetaInfo;
    }

}
