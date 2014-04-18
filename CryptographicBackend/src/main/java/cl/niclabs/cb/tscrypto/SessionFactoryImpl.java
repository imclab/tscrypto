package cl.niclabs.cb.tscrypto;

import cl.niclabs.cb.common.*;
import cl.niclabs.tscrypto.manager.keyManagement.KeyManager;
import cl.niclabs.tscrypto.manager.RequestManager;

public class SessionFactoryImpl implements SessionFactory {
    private final KeyManager keyManager;
    private final RequestManager requestManager;

    public SessionFactoryImpl(KeyManager keyManager, RequestManager requestManager) {
        this.keyManager = keyManager;
        this.requestManager = requestManager;
    }

    @Override
    public Session makeSession() {
        return new SessionImpl(new SignerImpl(keyManager, requestManager), new DigesterImpl(), new RandomGeneratorImpl());
    }
}
