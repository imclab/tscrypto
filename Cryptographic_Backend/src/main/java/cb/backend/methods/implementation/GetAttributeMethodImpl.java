package cb.backend.methods.implementation;

import cb.backend.KeyStorage;
import cb.backend.MapKeyStorage;
import cb.backend.ResponseMessage;
import cb.backend.methods.GetAttributeMethod;
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
