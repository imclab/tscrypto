package cl.niclabs.cb.backend.methods.implementation;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.GenerateRandomMethod;

import javax.xml.bind.DatatypeConverter;

public class GenerateRandomMethodImpl extends GenerateRandomMethod {
    public GenerateRandomMethodImpl(Args args) {
        super(args);
    }

    @Override
    public ResponseMessage execute() {
        byte[] randomBytes = RandomGenerator.getIntance().generateRandom(this.length);
        String rv = DatatypeConverter.printBase64Binary(randomBytes);
        return ResponseMessage.OKMessage(new ReturnValue(rv));
    }
}
