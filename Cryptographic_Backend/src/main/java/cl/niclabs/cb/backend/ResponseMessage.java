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
	private final Object value;
	
	public static ResponseMessage ErrorMessage(String rc) {
		return new ResponseMessage(rc, null);
	}

    public static ResponseMessage OKMessage() {
        return new ResponseMessage("OK", null);
    }

    public static ResponseMessage OKMessage(Object v) {
        return new ResponseMessage("OK", v);
    }
	
	private ResponseMessage(String rc, Object v) {
		returnCode = rc;
		value = v;
	}
	public String getReturnCode() {
		return returnCode;
	}
	
	public String getValue() {
        Gson gson = new Gson();
		return gson.toJson(value);
	}
	
}
