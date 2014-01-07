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

package cl.niclabs.cb.dispatcher;

import cl.niclabs.cb.backend.Method;
import cl.niclabs.cb.backend.methods.*;
import com.google.gson.*;

import java.lang.reflect.Type;

public class MethodParser implements JsonDeserializer<Method> {

    private final MethodFactory methodFactory;

    public MethodParser(MethodFactory methodFactory) {
        this.methodFactory = methodFactory;
    }

    public static Gson createGson(MethodFactory factory) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Method.class, new MethodParser(factory));
        return gsonBuilder.create();
    }

    @Override
    public Method deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String method = jsonObject.get("method").getAsString();
        JsonElement argsJsonElement = jsonObject.get("args");
        switch (method) {
            case "OpenSession":
            {
                return methodFactory.makeOpenSessionMethod();
            }
            case "CloseSession":
            {
                CloseSessionMethod.Args args = context.deserialize(argsJsonElement, CloseSessionMethod.Args.class);
                return methodFactory.makeCloseSessionMethod(args);
            }
            case "DeleteKeyPair":
            {
                DeleteKeyPairMethod.Args args = context.deserialize(argsJsonElement, DeleteKeyPairMethod.Args.class);
                return methodFactory.makeDeleteKeyMethod(args);
            }
            case "GenerateKeyPair":
            {
                GenerateKeyPairMethod.Args args = context.deserialize(argsJsonElement, GenerateKeyPairMethod.Args.class);
                return methodFactory.makeGenerateKeyPairMethod(args);
            }
            case "SignInit":
            {
                SignInitMethod.Args args = context.deserialize(argsJsonElement, SignInitMethod.Args.class);
                return methodFactory.makeSignInitMethod(args);
            }
            case "Sign":
            {
                SignMethod.Args args = context.deserialize(argsJsonElement, SignMethod.Args.class);
                return methodFactory.makeSignMethod(args);
            }
            case "FindKey":
            {
                FindKeyMethod.Args args = context.deserialize(argsJsonElement, FindKeyMethod.Args.class);
                return methodFactory.makeFindKeyMethod(args);
            }
            case "SeedRandom":
            {
                SeedRandomMethod.Args args = context.deserialize(argsJsonElement, SeedRandomMethod.Args.class);
                return methodFactory.makeSeedRandomMethod(args);
            }
            case "GenerateRandom":
            {
                GenerateRandomMethod.Args args = context.deserialize(argsJsonElement, GenerateRandomMethod.Args.class);
                return methodFactory.makeGenerateRandomMethod(args);
            }
            case "DigestInit":
            {
                DigestInitMethod.Args args = context.deserialize(argsJsonElement, DigestInitMethod.Args.class);
                return methodFactory.makeDigestInitMethod(args);
            }
            case "Digest":
            {
                DigestMethod.Args args = context.deserialize(argsJsonElement, DigestMethod.Args.class);
                return methodFactory.makeDigestMethod(args);
            }
            default:
            {
                throw new JsonParseException("Cannot parse method: " + method);
            }
        }

    }
}
