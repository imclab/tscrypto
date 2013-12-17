package cl.inria.tscrypto.common.datatypes;

import java.math.BigInteger;

public class SignatureShare {

	public BigInteger signature;
	public BigInteger c;
	public BigInteger z;
	
	/**
	 * no-args constructor required by Gson
	 */
	public SignatureShare() {
	}
	
	public SignatureShare(BigInteger signature, BigInteger c, BigInteger z) {
		this.signature = signature;
		this.c = c;
		this.z = z;
	}

}
