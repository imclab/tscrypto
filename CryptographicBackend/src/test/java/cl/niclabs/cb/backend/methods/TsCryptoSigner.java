package cl.niclabs.cb.backend.methods;

import cl.inria.tscrypto.common.utils.TSConnection;
import cl.inria.tscrypto.sigDealer.SDConfig;
import cl.niclabs.cb.backend.Method;
import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.dispatcher.MethodDispatcher;
import cl.niclabs.cb.tscrypto.methods.TsCryptoMethodFactory;
import com.google.gson.Gson;
import com.rabbitmq.client.Connection;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;

@RunWith(JUnit4.class)
public class TsCryptoSigner {

    @Test
    public void testAlgorithms() throws Exception {
        MethodFactory methodFactory;
        Gson gson;

        SDConfig config = new SDConfig();
        Connection connection = TSConnection.getConnection(config.getRabbitMQConfig());
        methodFactory = new TsCryptoMethodFactory(connection, config);
        gson = new Gson();

        Method method = methodFactory.makeGenerateKeyPairMethod(new GenerateKeyPairMethod.Args("RSA", 1024, "65537"));
        ResponseMessage rm = method.execute();
        System.out.println(gson.toJson(rm));
        String keyHandler = gson.fromJson(rm.getValue(), GenerateKeyPairMethod.ReturnValue.class).handler;

        method = methodFactory.makeOpenSessionMethod();
        rm = method.execute();
        String sessionHandler = gson.fromJson(rm.getValue(), OpenSessionMethod.ReturnValue.class).sessionHandler;

        Assert.assertEquals(rm.getReturnCode(), "OK");

        method = methodFactory.makeSignInitMethod(new SignInitMethod.Args(sessionHandler, "Sha1WithRSA", keyHandler));
        rm = method.execute();
        System.out.println(gson.toJson(rm));

        Assert.assertEquals(rm.getReturnCode(), "OK");

        String data = DatatypeConverter.printBase64Binary("Hola Mundo!".getBytes());
        method = methodFactory.makeSignMethod(new SignMethod.Args(sessionHandler, data));
        rm = method.execute();
        System.out.println(gson.toJson(rm));

        Assert.assertEquals(rm.getReturnCode(), "OK");

        method = methodFactory.makeDeleteKeyMethod(new DeleteKeyPairMethod.Args(keyHandler));
        rm = method.execute();
        System.out.println(gson.toJson(rm));

        Assert.assertEquals(rm.getReturnCode(), "OK");
    }

    @Ignore
    @Test
    public void methodDispatcherTest() throws IOException {
        SDConfig config = new SDConfig();
        Connection connection = TSConnection.getConnection(config.getRabbitMQConfig());

        String s = "{ \"method\":\"GenerateKeyPair\", "
                + " \"args\": { "
                + " \"label\" : \"hola mundo\", "
                + " \"id\":\"chao mundo\", "
                + " \"keyType\": \"RSA\", "
                + " \"keySize\":1024, "
                + " \"publicExponent\":65537"
                + " } "
                + "}";

        String ret = MethodDispatcher.dispatch(s, new TsCryptoMethodFactory(connection, config));
        System.out.println(ret);
    }
}
