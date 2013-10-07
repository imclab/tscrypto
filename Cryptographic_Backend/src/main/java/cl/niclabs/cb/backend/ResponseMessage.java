package cl.niclabs.cb.backend;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.Serializable;

public class ResponseMessage {
	@Override
	public String toString() {
		return "ResponseMessage [returnCode=" + returnCode + ", value=" + value
				+ "]";
	}

	private final String returnCode;
	private final JsonElement value;
	
	public static ResponseMessage ErrorMessage(String rc) {
		return new ResponseMessage(rc, null);
	}

    public static ResponseMessage OKMessage() {
        return new ResponseMessage("OK", null);
    }

	public static ResponseMessage OKMessage(String v) {
		return new ResponseMessage("OK", v);
	}

    public static ResponseMessage OKMessageWithObject(Object v) {
        Gson gson = new Gson();
        return new ResponseMessage("OK", gson.toJson(v));
    }
	
	private ResponseMessage(String rc, String v) {
		JsonParser parser = new JsonParser();
		returnCode = rc;
		value = v == null? null : parser.parse(v);
	}
	public String getReturnCode() {
		return returnCode;
	}
	
	public String getValue() {
		return value.toString();
	}
	
}
