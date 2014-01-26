package cl.niclabs.tscrypto.node;

import cl.niclabs.tscrypto.common.algorithms.signer.PlayerSigner;
import cl.niclabs.tscrypto.common.algorithms.signer.PlayerSignerImpl;
import cl.niclabs.tscrypto.common.algorithms.signer.PlayerSignerJniImpl;
import cl.niclabs.tscrypto.common.datatypes.KeyShareInfo;

public class PlayerSignerFactory {
    static public PlayerSigner getInstance(KeyShareInfo keyShareInfo, int id) {
        NodeConfig config = NodeConfig.getInstance();
        if (config.getUseJNI()) {
            return new PlayerSignerJniImpl(keyShareInfo, id);
        } else {
            return new PlayerSignerImpl(keyShareInfo, id);
        }
    }
}
