package cb.backend.methods;

import cb.backend.Method;
import cb.backend.ResponseMessage;
import cb.backend.Signer;
import com.google.gson.Gson;
import javax.xml.bind.DatatypeConverter;

public class SignMethodImpl implements Method {

  public static class ReturnValue {
    String signedData;
  }

  public static class Args {
    String data;
  }
  private byte[] data;

  public SignMethodImpl(String data) {
    this.data = DatatypeConverter.parseBase64Binary(data);
  }

  public SignMethodImpl(Args args) {
    data = DatatypeConverter.parseBase64Binary(args.data);
  }

  @Override
  public ResponseMessage execute() {
    Signer signer = Signer.getInstance();
    Gson gson = new Gson();
    ReturnValue rv = new ReturnValue();
    try {
      rv.signedData = DatatypeConverter.printBase64Binary(signer.sign(data));
      return ResponseMessage.OKMessage(gson.toJson(rv));
    } catch (Exception e) {
      return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
    }
  }

  public static void main(String[] args) {
    ResponseMessage rm;
    GenerateKeyPairMethodImpl.ReturnValue rv;
    Gson gson = new Gson();
    GenerateKeyPairMethodImpl gkpm = new GenerateKeyPairMethodImpl("RSA", 1024);
    SignInitMethodImpl sim;
    SignMethodImpl sm;
    
    

    rm = gkpm.execute();
    System.out.println(gson.toJson(rm));
    if (rm.getReturnCode().equals("OK")) {
      rv = gson.fromJson(rm.getValue(), GenerateKeyPairMethodImpl.ReturnValue.class);

      sim = new SignInitMethodImpl("SHA1withRSA", rv.handler);
      rm = sim.execute();
      System.out.println(gson.toJson(rm));

      if (rm.getReturnCode().equals("OK")) {
        sm = new SignMethodImpl(DatatypeConverter.printBase64Binary("Hola Mundo!".getBytes()));
        rm = sm.execute();
        System.out.println(gson.toJson(rm));
      }

    }

  }
}
