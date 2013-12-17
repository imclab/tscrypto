package cl.inria.tscrypto.common.datatypes;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;


public class TSPublicKey extends JsonFormat {

	/** The exponent of the groupKeyPair */
	public BigInteger e;

	/** The RSA modulus of the groupKeyPair */
	public BigInteger n;

	private BigInteger groupVerifier;
	private BigInteger[] shareVerifiers;

	/**
	 * Empty no-args constructor needed by Gson
	 */
	public TSPublicKey() {
	}
	
	public TSPublicKey(KeyMetaInfo keyMetaInfo, BigInteger e, BigInteger n, BigInteger groupVerifier, BigInteger[] shareVerifiers) {
		this.e = e;
		this.n = n;
		
		this.groupVerifier = groupVerifier;
		this.shareVerifiers = shareVerifiers;
	}

	/**
	 * Returns the group key modulus
	 * 
	 * @return This group's modulus.
	 */
	public BigInteger getModulus() {
		return this.n;
	}

	/**
	 * Returns the group key exponent
	 * 
	 * @return This group's exponent
	 */
	public BigInteger getExponent() {
		return this.e;
	}
	
	public BigInteger getShareVerifier(int id) {
		return this.shareVerifiers[id];
	}

	public BigInteger getGroupVerifier() {
		return this.groupVerifier;
	}
	
	public PublicKey convertoToPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec ks = new RSAPublicKeySpec(n, e);
		return (RSAPublicKey) keyFactory.generatePublic(ks);
	}

}
