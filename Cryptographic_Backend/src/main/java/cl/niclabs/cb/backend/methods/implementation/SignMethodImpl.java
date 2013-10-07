package cl.niclabs.cb.backend.methods.implementation;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.SignMethod;

import javax.xml.bind.DatatypeConverter;

class SignMethodImpl extends SignMethod {
    public SignMethodImpl(Args args) {
        super(args);
    }

    @Override
    public ResponseMessage execute() {
      Signer signer = Signer.getInstance();

      try {
        ReturnValue rv = new ReturnValue(DatatypeConverter.printBase64Binary(signer.sign(data)));
        return ResponseMessage.OKMessage(rv);
      } catch (Exception e) {
        return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
      }
    }
}
