package cb.dispatcher;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class MethodMessage {
	private String method;
	private JsonElement args;
	
	public MethodMessage(String method, String args) {
		JsonParser parser = new JsonParser();
		this.method = method;
		this.args = parser.parse(args);
	}
	
	public String getMethod() {
		return method;
	}
	
	public String getArgs() {
		return args.toString();
	}
	
}
