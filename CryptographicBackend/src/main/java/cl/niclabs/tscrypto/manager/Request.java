package cl.niclabs.tscrypto.manager;

import cl.niclabs.tscrypto.common.utils.TSLogger;

public abstract class Request {
    abstract public void setReady(int nodeId);
    abstract protected boolean isReady();

    public void waitUntilReady() throws InterruptedException {
        while (!isReady()) {
            synchronized (this) {
                while (!isReady()) {
                    TSLogger.sd.info("Waiting nodes...");
                    wait();
                }
            }
        }
    }
}
