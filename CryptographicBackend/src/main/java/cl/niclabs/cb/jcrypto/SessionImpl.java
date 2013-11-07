
package cl.niclabs.cb.jcrypto;

import cl.niclabs.cb.backend.Digester;
import cl.niclabs.cb.backend.RandomGenerator;
import cl.niclabs.cb.backend.Session;
import cl.niclabs.cb.backend.Signer;

import java.util.UUID;

public class SessionImpl implements Session {
    private UUID handler;
    private Signer signer;
    private Digester digester;
    private RandomGenerator randomGenerator;

    public SessionImpl() {
        this.handler = UUID.randomUUID();
        this.signer = new SignerImpl();
        this.digester = new DigesterImpl();
        this.randomGenerator = new RandomGeneratorImpl();
    }

    @Override
    public String getHandler() {
        return handler.toString();
    }

    @Override
    public Signer getSigner() {
        return signer;
    }

    @Override
    public Digester getDigester() {
        return digester;
    }

    @Override
    public RandomGenerator getRandomGenerator() {
        return randomGenerator;
    }

    @Override
    public String toString() {
        return getHandler();
    }
}
