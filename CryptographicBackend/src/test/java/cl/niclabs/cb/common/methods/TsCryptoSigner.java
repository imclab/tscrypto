/*
    Copyright 2013 NIC Chile Research Labs
    This file is part of TsCrypto.

    TsCrypto is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TsCrypto is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TsCrypto.  If not, see <http://www.gnu.org/licenses/>.
 */

package cl.niclabs.cb.common.methods;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class TsCryptoSigner {

    @Test
    public void testAlgorithms() throws Exception {
        /*
        MethodFactory methodFactory;
        Gson gson;

        SDConfig config = new SDConfig();
        Connection connection = TSConnection.getConnection(config.getRabbitMQConfig());
        KeyDispatcher keyDispatcher = new KeyDispatcher(connection, config.getRabbitMQConfig());

        methodFactory = new TsCryptoMethodFactory(connection, config, requestManager, keyDispatcher);
        gson = new Gson();

        Method method = methodFactory.makeGenerateKeyPairMethod(new GenerateKeyPairMethod.Args("RSA", 1024, "65537"));
        ResponseMessage rm = method.execute();
        System.out.println(gson.toJson(rm));
        String keyHandler = gson.fromJson(rm.getValue(), GenerateKeyPairMethod.GenerationResult.class).keyHandler;

        method = methodFactory.makeOpenSessionMethod();
        rm = method.execute();
        String sessionHandler = gson.fromJson(rm.getValue(), OpenSessionMethod.GenerationResult.class).sessionHandler;

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
        */
    }

    @Ignore
    @Test
    public void methodDispatcherTest() throws IOException {
        /*
        SDConfig config = new SDConfig();
        Connection connection = TSConnection.getConnection(config.getRabbitMQConfig());
        KeyDispatcher keyDispatcher = new KeyDispatcher(connection, config.getRabbitMQConfig());

        String s = "{ \"method\":\"GenerateKeyPair\", "
                + " \"args\": { "
                + " \"label\" : \"hola mundo\", "
                + " \"id\":\"chao mundo\", "
                + " \"keyType\": \"RSA\", "
                + " \"keySize\":1024, "
                + " \"publicExponent\":65537"
                + " } "
                + "}";

        String ret = MethodDispatcher.dispatch(s, new TsCryptoMethodFactory(connection, config, requestManager, keyDispatcher));
        System.out.println(ret);
        */
    }
}
