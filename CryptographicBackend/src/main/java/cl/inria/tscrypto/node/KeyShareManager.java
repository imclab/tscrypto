package cl.inria.tscrypto.node;

import cl.inria.tscrypto.common.datatypes.KeyShareInfo;
import cl.inria.tscrypto.common.datatypes.TSPublicKey;

/**
 * Created by franchoco on 19-12-13.
 */
public interface KeyShareManager {
    void addKey(String label, KeyShareInfo key);

    void removeKey(String label);

    TSPublicKey getPublicKey(String label);

    KeyShareInfo getKeyShareInfo(String label);
}
