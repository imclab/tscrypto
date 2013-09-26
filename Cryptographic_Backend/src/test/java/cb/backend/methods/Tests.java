package cb.backend.methods;

import cb.backend.*;
import cb.backend.methods.implementation.SimpleSignMethodFactory;
import cb.dispatcher.*;
import com.google.gson.Gson;

import javax.xml.bind.DatatypeConverter;

public class Tests {

    public static void signTest() {
        Gson gson = new Gson();

        ResponseMessage rm;
        Method method;
        long handler;

        MethodsFactory mf = SimpleSignMethodFactory.getInstance();

        method = mf.makeGenerateKeyPairMethod(new GenerateKeyPairMethod.Args("RSA", 1024, "65537"));
        rm = method.execute();
        System.out.println(gson.toJson(rm));
        if (!rm.getReturnCode().equals("OK"))
            return;

        handler = gson.fromJson(rm.getValue(), GenerateKeyPairMethod.ReturnValue.class).handler;
        method = mf.makeSignInitMethod(new SignInitMethod.Args("SHA1withRSA", handler));
        rm = method.execute();
        System.out.println(gson.toJson(rm));

        if (!rm.getReturnCode().equals("OK"))
            return;

        String data = DatatypeConverter.printBase64Binary("Hola Mundo!".getBytes());
        method = mf.makeSignMethod(new SignMethod.Args(data));
        rm = method.execute();
        System.out.println(gson.toJson(rm));

        if (!rm.getReturnCode().equals("OK"))
            return;

        method = mf.makeDeleteKeyMethod(new DeleteKeyPairMethod.Args(handler));
        rm = method.execute();
        System.out.println(gson.toJson(rm));
    }
    public static void methodDispatcherTest() {
        Gson gson = new Gson();

        String s = "{ \"method\":\"GenerateKeyPair\", "
                + " \"args\": { "
                + " \"label\" : \"hola mundo\", "
                + " \"id\":\"chao mundo\", "
                + " \"keyType\": \"RSA\", "
                + " \"keySize\":1024, "
                + " \"publicExponent\":65537"
                + " } "
                + "}";
        MethodMessage mm = gson.fromJson(s, MethodMessage.class);
        System.out.println(mm.getArgs());
        MethodDispatcher md = new MethodDispatcher(mm, SimpleSignMethodFactory.getInstance());
        String ret = md.dispatch();
        System.out.println(ret);
    }
    public static void main(String[] args) {
        signTest();
        methodDispatcherTest();
    }
}
