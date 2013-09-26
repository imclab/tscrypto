package cb.dispatcher;

import cb.backend.methods.*;
import com.google.gson.Gson;

import cb.backend.methods.FindKeyMethod;
import cb.backend.Method;
import cb.backend.ResponseMessage;

public class MethodDispatcher {

    public Method method = null;

    public MethodDispatcher(MethodMessage methodMsg, MethodsFactory methodsFactory) {
        Gson gson = new Gson();
        switch (methodMsg.getMethod()) {
            case "DeleteKeyMethod":
            {
                DeleteKeyMethod.Args args;
                args = gson.fromJson(methodMsg.getArgs(), DeleteKeyMethod.Args.class);
                method = methodsFactory.makeDeleteKeyMethod(args);

                break;
            }
            case "GenerateKeyPair":
            {
                GenerateKeyPairMethod.Args args;
                args = gson.fromJson(methodMsg.getArgs(), GenerateKeyPairMethod.Args.class);
                method = methodsFactory.makeGenerateKeyPairMethod(args);

                break;
            }
            case "SignInit":
            {
                SignInitMethod.Args args;
                args = gson.fromJson(methodMsg.getArgs(), SignInitMethod.Args.class);
                method = methodsFactory.makeSignInitMethod(args);

                break;
            }
            case "Sign":
            {
                SignMethod.Args args = gson.fromJson(methodMsg.getArgs(), SignMethod.Args.class);
                method = methodsFactory.makeSignMethod(args);

                break;
            }
            case "FindKey":
            {
                FindKeyMethod.Args args = gson.fromJson(methodMsg.getArgs(), FindKeyMethod.Args.class);
                method = methodsFactory.makeFindKeyMethod(args);

                break;
            }
            case "GetAttribute":
            {
                GetAttributeMethod.Args args = gson.fromJson(methodMsg.getArgs(), GetAttributeMethod.Args.class);
                method = methodsFactory.makeGetAttributeMethod(args);

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
