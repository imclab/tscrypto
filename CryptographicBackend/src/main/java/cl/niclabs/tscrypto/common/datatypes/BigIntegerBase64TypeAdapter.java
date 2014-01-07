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

package cl.niclabs.tscrypto.common.datatypes;
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
