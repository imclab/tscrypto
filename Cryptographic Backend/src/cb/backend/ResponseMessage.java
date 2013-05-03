package cb.backend;

public class ResponseMessage {
	private String returnCode;
	private String value;
	
	public static ResponseMessage ErrorMessage(String rc) {
		return new ResponseMessage(rc, null);
	}
	
	@Override
	public String toString() {
		return "ResponseMessage [returnCode=" + returnCode + ", value=" + value
				+ "]";
	}

	public static ResponseMessage OKMessage(String v) {
		return new ResponseMessage("OK", v);
	}
	
	private ResponseMessage(String rc, String v) {
		returnCode = rc;
		value = v;
	}
	public String getReturnCode() {
		return returnCode;
	}
	
	public String getValue() {
		return value;
	}
	
}
