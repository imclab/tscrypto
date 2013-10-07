package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.*;
import cl.niclabs.cb.backend.methods.implementation.SimpleSignMethodFactory;
import cl.niclabs.cb.dispatcher.*;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.xml.bind.DatatypeConverter;


@RunWith(JUnit4.class)
public class SimpleSigner {
    private Gson gson;

    @Before
    public void setUp() throws Exception {
        gson = new Gson();
    }

    @Test
    public void signTest() {

        MethodFactory mf = SimpleSignMethodFactory.getInstance();

        Method method = mf.makeGenerateKeyPairMethod(new GenerateKeyPairMethod.Args("RSA", 1024, "65537"));
        ResponseMessage rm = method.execute();
        System.out.println(gson.toJson(rm));

        Assert.assertEquals(rm.getReturnCode(), "OK");

        String handler = gson.fromJson(rm.getValue(), GenerateKeyPairMethod.ReturnValue.class).handler;
        method = mf.makeSignInitMethod(new SignInitMethod.Args("SHA1withRSA", handler));
        rm = method.execute();
        System.out.println(gson.toJson(rm));

        Assert.assertEquals(rm.getReturnCode(), "OK");

        String data = DatatypeConverter.printBase64Binary("Hola Mundo!".getBytes());
        method = mf.makeSignMethod(new SignMethod.Args(data));
        rm = method.execute();
        System.out.println(gson.toJson(rm));

        Assert.assertEquals(rm.getReturnCode(), "OK");

        method = mf.makeDeleteKeyMethod(new DeleteKeyPairMethod.Args(handler));
        rm = method.execute();
        System.out.println(gson.toJson(rm));

        Assert.assertEquals(rm.getReturnCode(), "OK");
    }

    @Ignore
    @Test
    public void methodDispatcherTest() {
        String s = "{ \"method\":\"GenerateKeyPair\", "
                + " \"args\": { "
                + " \"label\" : \"hola mundo\", "
                + " \"id\":\"chao mundo\", "
                + " \"keyType\": \"RSA\", "
                + " \"keySize\":1024, "
                + " \"publicExponent\":65537"
                + " } "
                + "}";

        String ret = MethodDispatcher.dispatch(s, SimpleSignMethodFactory.getInstance());
        System.out.println(ret);
    }
}
