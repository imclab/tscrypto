package cl.niclabs.cb.dispatcher;

import cl.niclabs.cb.backend.methods.*;
import com.google.gson.Gson;

import cl.niclabs.cb.backend.methods.FindKeyMethod;
import cl.niclabs.cb.backend.Method;
import cl.niclabs.cb.backend.ResponseMessage;

public class MethodDispatcher {

    public Method method = null;

    public MethodDispatcher(MethodMessage methodMsg, MethodFactory methodFactory) {
        Gson gson = new Gson();
        switch (methodMsg.getMethod()) {
            case "DeleteKeyPair":
            {
                DeleteKeyPairMethod.Args args;
                args = gson.fromJson(methodMsg.getArgs(), DeleteKeyPairMethod.Args.class);
                method = methodFactory.makeDeleteKeyMethod(args);

                break;
            }
            case "GenerateKeyPair":
            {
                GenerateKeyPairMethod.Args args;
                args = gson.fromJson(methodMsg.getArgs(), GenerateKeyPairMethod.Args.class);
                method = methodFactory.makeGenerateKeyPairMethod(args);

                break;
            }
            case "SignInit":
            {
                SignInitMethod.Args args;
                args = gson.fromJson(methodMsg.getArgs(), SignInitMethod.Args.class);
                method = methodFactory.makeSignInitMethod(args);

                break;
            }
            case "Sign":
            {
                SignMethod.Args args = gson.fromJson(methodMsg.getArgs(), SignMethod.Args.class);
                method = methodFactory.makeSignMethod(args);

                break;
            }
            case "FindKey":
            {
                FindKeyMethod.Args args = gson.fromJson(methodMsg.getArgs(), FindKeyMethod.Args.class);
                method = methodFactory.makeFindKeyMethod(args);

                break;
            }
            case "GetAttribute":
            {
                GetAttributeMethod.Args args = gson.fromJson(methodMsg.getArgs(), GetAttributeMethod.Args.class);
                method = methodFactory.makeGetAttributeMethod(args);

                break;
            }
        }

    }

    public String dispatch() {
        Gson gson = new Gson();
        if (method == null) { // Es feo esto, pero es rapido de programar :P.
            return gson.toJson(ResponseMessage.ErrorMessage("Metodo no valido"));
        }

        ResponseMessage rm = method.execute();
        return gson.toJson(rm);
    }
}
