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

package cl.niclabs.tscrypto.common.datatypes;

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
		return keyFactory.generatePublic(ks);
	}

}
