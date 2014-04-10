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

import cl.niclabs.cb.common.Method;
import cl.niclabs.cb.common.ResponseMessage;
import cl.niclabs.cb.common.SessionManagerImpl;
import cl.niclabs.cb.jcrypto.KeyOperationsImpl;
import cl.niclabs.cb.jcrypto.MapKeyStorage;
import cl.niclabs.cb.jcrypto.SessionFactoryImpl;
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

        MethodFactory mf = new MethodFactoryImpl(
                new SessionManagerImpl(),
                new SessionFactoryImpl(),
                new KeyOperationsImpl(new MapKeyStorage())
                );

        Method method = mf.makeGenerateKeyPairMethod(new GenerateKeyPairMethod.Args("RSA", 1024, "65537"));
        System.out.println(gson.toJson(method));
        ResponseMessage rm = method.execute();
        System.out.println(gson.toJson(rm));
        String keyHandler = gson.fromJson(rm.getValue(), GenerateKeyPairMethod.ReturnValue.class).keyHandler;

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
