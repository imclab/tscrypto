package cb.dispatcher;

import com.google.gson.Gson;

import cb.backend.GenerateKeyPairMethod;
import cb.backend.Method;
import cb.backend.ResponseMessage;
import cb.backend.SignInitMethod;
import cb.backend.SignMethod;

public class MethodDispatcher {
	
	public Method method;
	
	public MethodDispatcher(MethodMessage methodMsg) {
		Gson gson = new Gson();
		switch (methodMsg.getMethod()) {
		case "GenerateKeyPair":
		{
			GenerateKeyPairMethod.Args args;
			args = gson.fromJson(methodMsg.getArgs(), GenerateKeyPairMethod.Args.class);
			method = new GenerateKeyPairMethod (args);

			break;
		}
		case "SignInit":
		{
			SignInitMethod.Args args;
			args = gson.fromJson(methodMsg.getArgs(), SignInitMethod.Args.class);
			method = new SignInitMethod (args);
			
			break;
		}
		case "Sign":
		{
			SignMethod.Args args = gson.fromJson(methodMsg.getArgs(), SignMethod.Args.class);
			method = new SignMethod (args);

			break;
		}
		}
	}
	
	public String dipatch () {
		Gson gson = new Gson();
		ResponseMessage rm = method.execute();
		return gson.toJson(rm);
	}
	
	public static void main (String[] args) {
		Gson gson = new Gson();
		
		String s = "{ \"method\":\"GenerateKeyPair\", " 
				+ " \"args\": { "
				+ " \"label\" : \"hola mundo\", "
				+ " \"id\":\"chao mundo\", " 
				+ " \"keyType\": \"RSA\", "
				+ " \"keySize\":1024 } }";
		MethodMessage mm = gson.fromJson(s, MethodMessage.class);
		System.out.println(mm.getArgs());
		MethodDispatcher md = new MethodDispatcher(mm);
		String ret = md.dipatch();
		System.out.println(ret);
	}
}
