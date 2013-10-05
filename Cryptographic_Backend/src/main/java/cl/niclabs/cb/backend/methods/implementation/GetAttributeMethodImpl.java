package cl.niclabs.cb.backend.methods.implementation;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.GetAttributeMethod;
import com.google.gson.Gson;

class GetAttributeMethodImpl extends GetAttributeMethod {
    public GetAttributeMethodImpl(Args args) {
        super(args);
    }

    @Override
    public ResponseMessage execute() {
        Gson gson = new Gson();
        KeyStorage ks = MapKeyStorage.getInstance();

        try {
            ReturnValue rv = new ReturnValue(ks.getAttribute(attribute, handler));
            return ResponseMessage.OKMessage(gson.toJson(rv));
        }
        catch(Exception e) {
            return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
        }
    }
}
