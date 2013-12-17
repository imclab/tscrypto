package cl.inria.tscrypto.utils;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;

public class RSASign {

	BigInteger n, e, d;

	public RSASign(BigInteger n, BigInteger e, BigInteger d) {
		// TODO Auto-generated constructor stub
		this.n = n;
		this.e = e;
		this.d = d;
	}

	public BigInteger sign(String data, String algorithm)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidKeyException, SignatureException {

		// sing with java

		RSAPrivateKeySpec sspec = new RSAPrivateKeySpec(this.n, this.d);
		KeyFactory factory = KeyFactory.getInstance("RSA");
		PrivateKey skey = factory.generatePrivate(sspec);

		// MessageDigest dig = MessageDigest.getInstance("SHA1");
		// byte hdata[] = dig.digest(data);

		Signature sign = Signature.getInstance(algorithm);
		sign.initSign(skey);
		sign.update(data.getBytes());

		byte[] javaSign = sign.sign();

		/*
		 * System.out.println("Dealer:\t" + signature.toString(16)); System.out
		 * .println("Java:\t" + new BigInteger(1, javaSign).toString(16));
		 * 
		 * byte aux[] = signature.toByteArray(); int size = (int)
		 * Math.ceil(n.bitLength() / 8); byte[] mySignature = new byte[size];
		 * System.arraycopy(aux, aux.length - size, mySignature, 0, size);
		 * 
		 * System.out.println("Dealer:\t" +
		 * Encoder.byteArrayToHexString(mySignature));
		 * System.out.println("Java:\t" +
		 * Encoder.byteArrayToHexString(javaSign));
		 */

		BigInteger finalSignature = new BigInteger(javaSign);
		return finalSignature;
	}

}