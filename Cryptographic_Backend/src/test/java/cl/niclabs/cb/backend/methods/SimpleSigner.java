package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;
import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.jcrypto.methods.SimpleSignMethodFactory;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
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
        System.out.println(gson.toJson(method));
        ResponseMessage rm = method.execute();
        System.out.println(gson.toJson(rm));
        String keyHandler = gson.fromJson(rm.getValue(), GenerateKeyPairMethod.ReturnValue.class).handler;

        Assert.assertEquals(rm.getReturnCode(), "OK");

        method = mf.makeOpenSessionMethod();
        System.out.println(gson.toJson(method));
        rm = method.execute();
        System.out.println(gson.toJson(rm));
        String sessionHandler = gson.fromJson(rm.getValue(), OpenSessionMethod.ReturnValue.class).sessionHandler;

        method = mf.makeSignInitMethod(new SignInitMethod.Args(sessionHandler, "SHA1withRSA", keyHandler));
        System.out.println(gson.toJson(method));
        rm = method.execute();
        System.out.println(gson.toJson(rm));

        Assert.assertEquals(rm.getReturnCode(), "OK");

        String data = DatatypeConverter.printBase64Binary("Hola Mundo!".getBytes());
        System.out.println(gson.toJson(method));
        method = mf.makeSignMethod(new SignMethod.Args(sessionHandler, data));
        rm = method.execute();
        System.out.println(gson.toJson(rm));

        Assert.assertEquals(rm.getReturnCode(), "OK");

        method = mf.makeDeleteKeyMethod(new DeleteKeyPairMethod.Args(keyHandler));
        System.out.println(gson.toJson(method));
        rm = method.execute();
        System.out.println(gson.toJson(rm));

        Assert.assertEquals(rm.getReturnCode(), "OK");
    }
}
