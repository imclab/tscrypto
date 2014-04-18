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

package cl.niclabs.tscrypto.common.utils;

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
