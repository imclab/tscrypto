package cb.backend;

import com.google.gson.Gson;

public class FindKeyMethod implements Method {
	public static class ReturnValue {
		public int handler;
	}
	public static class Args {
		public String keyType; // Dummy :P...
		public String id;
	}
	private String id;

	public FindKeyMethod(Args args) {
		this.id = args.id;
	}
	
	@Override
	public ResponseMessage execute() {
		Gson gson = new Gson();
		ReturnValue rv = new ReturnValue();
		KeyStorage ks = MapKeyStorage.getInstance();
		try {
			rv.handler = ks.findHandler(id);
			return ResponseMessage.OKMessage(gson.toJson(rv));
		}
		catch(Exception e) {
			return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
		}
	}

}
