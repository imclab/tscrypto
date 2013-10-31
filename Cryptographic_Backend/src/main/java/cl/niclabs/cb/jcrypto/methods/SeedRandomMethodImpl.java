package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.SeedRandomMethod;
import cl.niclabs.cb.jcrypto.RandomGeneratorImpl;

import javax.xml.bind.DatatypeConverter;

public class SeedRandomMethodImpl implements SeedRandomMethod {
    protected final byte[] seed;

    public SeedRandomMethodImpl(Args args) {
        seed = DatatypeConverter.parseBase64Binary(args.seed);
    }

    @Override
    public ResponseMessage execute() {
        RandomGeneratorImpl.getIntance().setSeed(seed);
        return ResponseMessage.OKMessage();
    }
}
