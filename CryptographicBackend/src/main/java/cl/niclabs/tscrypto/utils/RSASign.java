/*
    Copyright 2013 NIC Chile Research Labs
    This file is part of TsCrypto.

    TsCrypto is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TsCrypto is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TsCrypto.  If not, see <http://www.gnu.org/licenses/>.
 */

package cl.niclabs.tscrypto.utils;

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