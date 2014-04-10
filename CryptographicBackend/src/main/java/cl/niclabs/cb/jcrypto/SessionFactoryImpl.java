package cl.niclabs.cb.jcrypto;

import cl.niclabs.cb.common.*;

public class SessionFactoryImpl implements SessionFactory {
    @Override
    public Session makeSession() {
        return new SessionImpl(new SignerImpl(), new DigesterImpl(), new RandomGeneratorImpl());
    }
}
