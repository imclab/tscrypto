package cl.inria.tscrypto.sigDealer.utils;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

public class VerifyShare {

	public static boolean verify(String message, String signature, BigInteger n,
			BigInteger e) {
		byte[] data = message.getBytes();

		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
		KeyFactory fact;
		Signature sigTest;
		try {
			fact = KeyFactory.getInstance("RSA");
			PublicKey key = fact.generatePublic(keySpec);
			sigTest = Signature.getInstance("Sha1WithRSA");
			sigTest.initVerify(key);
			sigTest.update(data);
			return sigTest.verify((new BigInteger(signature)).toByteArray());
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidKeySpecException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (InvalidKeyException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} catch (SignatureException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
		return false;
	}
}
