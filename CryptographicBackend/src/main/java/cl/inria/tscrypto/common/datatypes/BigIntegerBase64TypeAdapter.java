package cl.inria.tscrypto.common.datatypes;
import java.lang.reflect.Type;
import java.math.BigInteger;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

	public class BigIntegerBase64TypeAdapter implements JsonSerializer<BigInteger>, JsonDeserializer<BigInteger> {

	@Override
	public JsonElement serialize(BigInteger src, Type typeOfSrc,
			JsonSerializationContext context) {
		return new JsonPrimitive(new String(Base64.encodeBase64(src.toByteArray())));
	}

	@Override
	public BigInteger deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
	    return new BigInteger(Base64.decodeBase64(json.getAsString()));
	}
}
