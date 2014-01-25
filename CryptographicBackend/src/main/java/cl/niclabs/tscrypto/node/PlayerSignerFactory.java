package cl.niclabs.tscrypto.node;

import cl.niclabs.tscrypto.common.algorithms.PlayerSigner;
import cl.niclabs.tscrypto.common.algorithms.PlayerSignerImpl;
import cl.niclabs.tscrypto.common.algorithms.PlayerSignerJniImpl;
import cl.niclabs.tscrypto.common.datatypes.KeyShareInfo;
import cl.niclabs.tscrypto.node.NodeConfig;

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
