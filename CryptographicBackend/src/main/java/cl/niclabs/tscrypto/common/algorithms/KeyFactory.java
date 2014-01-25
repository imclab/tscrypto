package cl.niclabs.tscrypto.common.algorithms;

import cl.niclabs.tscrypto.common.datatypes.KeyInfo;

/**
 * Created by franchoco on 25-01-14.
 */
public interface KeyFactory {
    KeyInfo generateKeys(int primesize, int k, int l);
}
