package cb.backend;

import com.google.gson.Gson;

public class GetAttributeMethod implements Method {
	static public class ReturnValue {
		public String attributeValue;
	}
	static public class Args {
		public String attribute;
		public int handler;
	}
	public String attribute;
	public int handler;
	
	public GetAttributeMethod(Args args) {
		this.attribute = args.attribute;
		this.handler = args.handler;
	}
	@Override
	public ResponseMessage execute() {
		Gson gson = new Gson();
		KeyStorage ks = MapKeyStorage.getInstance();
		
		try {
			ReturnValue rv = new ReturnValue(); 
			rv.attributeValue = ks.getAttribute(attribute, handler);
			return ResponseMessage.OKMessage(gson.toJson(rv));
		}
		catch(Exception e) {
			return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
		}
	}

}
