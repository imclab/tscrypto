package cl.niclabs.cb.backend.methods.implementation;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.SeedRandomMethod;

public class SeedRandomMethodImpl extends SeedRandomMethod {
    public SeedRandomMethodImpl(Args args) {
        super(args);
    }

    @Override
    public ResponseMessage execute() {
        RandomGenerator.getIntance().setSeed(seed);
        return ResponseMessage.OKMessage();
    }
}
