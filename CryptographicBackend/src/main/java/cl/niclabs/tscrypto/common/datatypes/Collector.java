package cl.niclabs.tscrypto.common.datatypes;

public interface Collector {
    public void start();
    public void stop() throws InterruptedException;
}
