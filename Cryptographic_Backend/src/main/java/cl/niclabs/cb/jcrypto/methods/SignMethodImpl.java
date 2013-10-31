package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.SignMethod;
import cl.niclabs.cb.jcrypto.Signer;
import cl.niclabs.cb.jcrypto.SignerImpl;

import javax.xml.bind.DatatypeConverter;

class SignMethodImpl implements SignMethod {
    protected final byte[] data;

    public SignMethodImpl(Args args) {
        data = DatatypeConverter.parseBase64Binary(args.data);
    }

    @Override
    public ResponseMessage execute() {
      Signer signer = SignerImpl.getInstance();

      try {
        ReturnValue rv = new ReturnValue(DatatypeConverter.printBase64Binary(signer.sign(data)));
        return ResponseMessage.OKMessage(rv);
      } catch (Exception e) {
        return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
      }
    }
}
