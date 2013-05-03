package cb.dispatcher;

import com.google.gson.Gson;
import com.google.gson.JsonElement;


public class MethodMessage {
	private String method;
	private JsonElement args;
	
	public MethodMessage(String method, String args) {
		Gson gson = new Gson();
		this.method = method;
		this.args = gson.toJsonTree(args);
	}
	
	public String getMethod() {
		return method;
	}
	
	public String getArgs() {
		return args.toString();
	}
	
}
