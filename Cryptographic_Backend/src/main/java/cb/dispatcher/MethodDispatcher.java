package cb.dispatcher;

import cb.backend.methods.*;
import com.google.gson.Gson;

import cb.backend.methods.FindKeyMethodImpl;
import cb.backend.Method;
import cb.backend.ResponseMessage;

public class MethodDispatcher {
	
	public Method method = null;
	
	public MethodDispatcher(MethodMessage methodMsg) {
		Gson gson = new Gson();
		switch (methodMsg.getMethod()) {
		case "GenerateKeyPair":
		{
			GenerateKeyPairMethodImpl.Args args;
			args = gson.fromJson(methodMsg.getArgs(), GenerateKeyPairMethodImpl.Args.class);
			method = new GenerateKeyPairMethodImpl(args);

			break;
		}
		case "SignInit":
		{
			SignInitMethodImpl.Args args;
			args = gson.fromJson(methodMsg.getArgs(), SignInitMethodImpl.Args.class);
			method = new SignInitMethodImpl(args);
			
			break;
		}
		case "Sign":
		{
			SignMethodImpl.Args args = gson.fromJson(methodMsg.getArgs(), SignMethodImpl.Args.class);
			method = new SignMethodImpl(args);

			break;
		}
		case "FindKey":
		{
			FindKeyMethodImpl.Args args = gson.fromJson(methodMsg.getArgs(), FindKeyMethodImpl.Args.class);
			method = new FindKeyMethodImpl(args);
			
			break;
		}
		case "GetAttribute":
		{
			GetAttributeMethodImpl.Args args = gson.fromJson(methodMsg.getArgs(), GetAttributeMethodImpl.Args.class);
			method = new GetAttributeMethodImpl(args);
			
			break;
		}
		}
			
	}
	
	public String dipatch () {
		Gson gson = new Gson();
		if (method != null) { // Es feo esto, pero es rapido de programar :P.
			
			ResponseMessage rm = method.execute();
			
			return gson.toJson(rm);
		}
		
		return gson.toJson(ResponseMessage.ErrorMessage("Metodo no valido"));
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
