package cl.niclabs.tscrypto.manager;

public interface Request {
    public void setReady(int nodeId);
    public boolean isReady();
    public void waitUntilReady() throws InterruptedException;
}
