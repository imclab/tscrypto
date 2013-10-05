package cl.niclabs.cb.backend.methods.implementation;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.SignMethod;
import com.google.gson.Gson;

import javax.xml.bind.DatatypeConverter;

class SignMethodImpl extends SignMethod {
    public SignMethodImpl(Args args) {
        super(args);
    }

    @Override
    public ResponseMessage execute() {
      Signer signer = Signer.getInstance();
      Gson gson = new Gson();
      try {
        ReturnValue rv = new ReturnValue(DatatypeConverter.printBase64Binary(signer.sign(data)));
        return ResponseMessage.OKMessage(gson.toJson(rv));
      } catch (Exception e) {
        return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
      }
    }
}
