package cb.backend;

import com.google.gson.Gson;


public class SignMethod implements Method {
	public static class ReturnValue {
		String signedData;
	}
	public static class Args {
		String data;
	}

	private byte[] data;
	
	public SignMethod(String hexData) {
		data = Signer.hexToBytes(hexData);
	}
	
	public SignMethod(Args args) {
		data = Signer.hexToBytes(args.data);
	}
	
	@Override
	public ResponseMessage execute() {
		Signer signer = Signer.getInstance();
		Gson gson = new Gson();
		ReturnValue rv = new ReturnValue();
		try {
			rv.signedData = Signer.bytesToHex(signer.sign(data));
			return ResponseMessage.OKMessage(gson.toJson(rv)); 
		} catch(Exception e) {
			return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
		}
	}
	
	public static void main (String[] args) {
		ResponseMessage rm;
		GenerateKeyPairMethod.ReturnValue rv;
		Gson gson = new Gson();
		GenerateKeyPairMethod gkpm = 
				new GenerateKeyPairMethod("hola", "mundo", "RSA", 1024);
		SignInitMethod sim;
		SignMethod sm;
		
		rm = gkpm.execute();
		System.out.println(rm);
		if (rm.getReturnCode().equals("OK")) {
			rv = gson.fromJson (rm.getValue(), GenerateKeyPairMethod.ReturnValue.class);
			
			sim = new SignInitMethod("SHA1withRSA", rv.privateKeyHandler);
			rm = sim.execute();
			System.out.println(rm);
			
			if (rm.getReturnCode().equals("OK")) {
				sm = new SignMethod(Signer.bytesToHex("Hola Mundo!".getBytes()));
				rm = sm.execute();
				System.out.println(rm);
			}
			
		}
		
	}

}
