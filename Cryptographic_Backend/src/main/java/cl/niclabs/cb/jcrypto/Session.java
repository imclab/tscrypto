
package cl.niclabs.cb.jcrypto;

import java.util.UUID;

public class Session {
    private UUID handler;
    private Signer signer;
    private Digester digester;
    private RandomGenerator randomGenerator;

    public Session() {
        this.handler = UUID.randomUUID();
        this.signer = new SignerImpl();
        this.digester = new DigesterImpl();
        this.randomGenerator = new RandomGeneratorImpl();
    }

    public String getHandler() {
        return handler.toString();
    }

    public Signer getSigner() {
        return signer;
    }

    public Digester getDigester() {
        return digester;
    }

    public RandomGenerator getRandomGenerator() {
        return randomGenerator;
    }

    @Override
    public String toString() {
        return getHandler();
    }
}
