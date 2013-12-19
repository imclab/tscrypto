package cl.inria.tscrypto.sigDealer;

import cl.inria.tscrypto.common.datatypes.KeyInfo;
import cl.inria.tscrypto.common.datatypes.KeyMetaInfo;
import cl.inria.tscrypto.common.datatypes.TSPublicKey;

public interface KeyManager {
    void addKey(KeyInfo key);

    void removeKey(String label);

    boolean containsKey(String label);

    TSPublicKey getPublicKey(String label);

    KeyMetaInfo getKeyMetaInfo(String label);
}
