package cl.inria.tscrypto.keyFactory;

import cl.inria.tscrypto.common.datatypes.KeyInfo;
import cl.inria.tscrypto.common.utils.TSLogger;

public class KeyDispatchRequest {
    private final boolean[] dispatchedKeys;

    public KeyDispatchRequest(KeyInfo keyInfo) {
        int l = keyInfo.getKeyMetaInfo().getL();
        this.dispatchedKeys = new boolean[l];
        for(int i=0; i<l; i++) {
            dispatchedKeys[i] = false;
        }
    }

    public synchronized void setReady(int id) {
        dispatchedKeys[id] = true;
        notifyAll();
    }

    public boolean isReady() {
        for(boolean ready: dispatchedKeys) {
            if(!ready)
                return false;
        }
        return true;
    }

    public void waitUntilReady() throws InterruptedException {
        while (!isReady()) {
            synchronized (this) {
                while (!isReady()) {
                    TSLogger.keyFactory.info("Waiting nodes to notify they added the key...");
                    wait();
                }
            }
        }
    }


}
