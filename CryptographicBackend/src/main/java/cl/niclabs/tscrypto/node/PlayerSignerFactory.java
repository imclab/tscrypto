package cl.niclabs.tscrypto.node;

import cl.niclabs.tscrypto.common.algorithms.signer.*;
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
            if(numThreads > 1){
                return new PlayerSignerThreadsImpl(keyShareInfo,id,numThreads);
            }
            else{
                return new PlayerSignerImpl(keyShareInfo, id);
            }

        }
    }
}
