package cl.inria.tscrypto.common.exceptions;

public class InvalidKey extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4314408624280496783L;
	private String alias;
	
	public InvalidKey(String alias) {
		this.alias = alias;
	}
	
	public String getAlias() {
		return alias;
	}

}
