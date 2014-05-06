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

package cl.niclabs.tscrypto.common.messages;

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

        if (type.equals("encrypted-data")) {
            return context.deserialize(json, EncryptedData.class);
        } else if (type.matches(".*-query")) {
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
