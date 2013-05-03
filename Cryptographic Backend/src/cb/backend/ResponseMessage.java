package cb.backend;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ResponseMessage {
	private String returnCode;
	private JsonElement value;
	
	public static ResponseMessage ErrorMessage(String rc) {
		return new ResponseMessage(rc, null);
	}

	public static ResponseMessage OKMessage(String v) {
		return new ResponseMessage("OK", v);
	}
	
	private ResponseMessage(String rc, String v) {
		JsonParser parser = new JsonParser();
		returnCode = rc;
		value = parser.parse(v);
	}
	public String getReturnCode() {
		return returnCode;
	}
	
	public String getValue() {
		return value.toString();
	}
	
}
