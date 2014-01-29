package cl.niclabs.tscrypto.node;

import cl.niclabs.tscrypto.common.algorithms.signer.PlayerSigner;
import cl.niclabs.tscrypto.common.algorithms.signer.PlayerSignerImpl;
import cl.niclabs.tscrypto.common.algorithms.signer.PlayerSignerJniImpl;
import cl.niclabs.tscrypto.common.algorithms.signer.PlayerSignerJniThreadsImpl;
import cl.niclabs.tscrypto.common.datatypes.KeyShareInfo;

public class PlayerSignerFactory {
    static public PlayerSigner getInstance(KeyShareInfo keyShareInfo, int id) {
        NodeConfig config = NodeConfig.getInstance();
        boolean useJNI = config.getUseJNI();
        int numThreads = config.getNumThreads();

        if (useJNI) {
            if(numThreads > 1) {
                return new PlayerSignerJniThreadsImpl(keyShareInfo, id, numThreads);
            } else {
                return new PlayerSignerJniImpl(keyShareInfo, id);
            }
        } else {
            return new PlayerSignerImpl(keyShareInfo, id);
        }
    }
}
