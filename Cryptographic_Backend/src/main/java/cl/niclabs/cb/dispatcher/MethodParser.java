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
