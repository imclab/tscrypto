package cl.inria.tscrypto.common.messages;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class TSMessageParser implements JsonDeserializer<TSMessage> {

    @Override
    public TSMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jobject = (JsonObject) json;
        String type = jobject.get("type").getAsString();
        String version = jobject.get("version").getAsString();

        // TODO check version
        
        if (type.matches(".*-query")) {
                return parseQuery(type, version, json, context);
        } else if (type.matches(".*-answer")) {
            return parseAnswer(type, version, json, context);
        }

        throw new JsonParseException("Illegal TSMessage type/version:" + type + "/" + version);
    }

    private TSMessage parseQuery(String type, String version, JsonElement json, JsonDeserializationContext context) {

        switch (type) {
            case "ping-query":
                return context.deserialize(json, PingQuery.class);
            case "signShare-query":
                return context.deserialize(json, SignShareQuery.class);
            case "sendKey-query":
                return context.deserialize(json, SendKeyQuery.class);
            case "deleteKey-query":
                return context.deserialize(json, DeleteKeyQuery.class);
        }
        
        throw new JsonParseException("Illegal TSMessage Query type/version:" + type + "/" + version);

    }

    private TSMessage parseAnswer(String type, String version, JsonElement json, JsonDeserializationContext context) {

        switch (type) {
            case "ping-answer":
                return context.deserialize(json, PingAnswer.class);
            case "signShare-answer":
                return context.deserialize(json, SignShareAnswer.class);
            case "sendKey-answer":
                return context.deserialize(json, SendKeyAnswer.class);
            case "deleteKey-answer":
                return context.deserialize(json, DeleteKeyAnswer.class);
        }
        
        throw new JsonParseException("Illegal TSMessage Answer type/version:" + type + "/" + version);

    }

}
