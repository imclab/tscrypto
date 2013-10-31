package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.GenerateRandomMethod;
import cl.niclabs.cb.jcrypto.RandomGeneratorImpl;

import javax.xml.bind.DatatypeConverter;

public class GenerateRandomMethodImpl implements GenerateRandomMethod {
    public final int length;

    public GenerateRandomMethodImpl(Args args) {
        length = args.length;
    }

    @Override
    public ResponseMessage execute() {
        byte[] randomBytes = RandomGeneratorImpl.getIntance().generateRandom(this.length);
        String rv = DatatypeConverter.printBase64Binary(randomBytes);
        return ResponseMessage.OKMessage(new ReturnValue(rv));
    }
}
