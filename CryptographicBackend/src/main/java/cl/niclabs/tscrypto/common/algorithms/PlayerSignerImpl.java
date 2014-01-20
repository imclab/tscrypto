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

package cl.niclabs.tscrypto.common.algorithms;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import cl.niclabs.tscrypto.common.datatypes.KeyShareInfo;
import cl.niclabs.tscrypto.common.datatypes.SignatureShare;
import cl.niclabs.tscrypto.common.datatypes.TSPublicKey;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.common.utils.ThreshUtil;

/**
 * A Secret Key Share for an RSA (k,l) Threshold Scheme
 * 
 * k: minimum number of uncorrupted players
 * l: total number of players
 * 
 * Reference: "Practical Threshold Signatures",<br>
 * Victor Shoup (sho@zurich.ibm.com), IBM Research Paper RZ3121, 4/30/99<BR>
 * 
 * @author Steve Weis <sweis@mit.edu>
 * @author acansado
 */
public class PlayerSignerImpl implements PlayerSigner {

	/** player id */
	private int id;

	/** l! the factorial of the number of players */
	private BigInteger delta;

	/** The secret key value used to sign messages */
	private BigInteger e_prime;

	/** The share hold to this player */
	private BigInteger secretShare;

	/** Public key information (includes verifiers) */
	private TSPublicKey publicKey;

	private static SecureRandom random;
	private MessageDigest md;

	/** lock to be used with Message Digest since it is not thread-safe */
	private static Object lockMd = new Object();

	static {
		final byte[] randSeed = new byte[20];
		(new Random()).nextBytes(randSeed);
		random = new SecureRandom(randSeed);
	}

        /**
         * Player signer in charge of a keyShare
         * @param key key used by this player
         * @param id player id
         */
	public PlayerSignerImpl(KeyShareInfo key, int id) {
		
		TSLogger.node.info("Creating player " + id);
		TSLogger.node.debug(key.toJson(true));

		this.secretShare = key.getShare();
		this.publicKey = key.getPublicKey();
		this.id = id;

		this.delta = ThreshUtil.factorial(key.getKeyMetaInfo().getL());
		this.e_prime = ThreshUtil.FOUR.multiply(delta).multiply(secretShare);
        //this.e_prime = ThreshUtil.TWO.multiply(delta).multiply(secretShare);
	}

	// Public Methods
	// ............................................................................

	/* (non-Javadoc)
	 * @see PlayerSigner#getId()
	 */
	@Override
	public int getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see PlayerSigner#sign(java.math.BigInteger)
	 */
	@Override
	public SignatureShare sign(BigInteger document) throws NoSuchAlgorithmException {

		// r \elt (0, 2^L(n)+3*l1)

		final int randbits = publicKey.getModulus().bitLength() + 3 * ThreshUtil.L1;
		final BigInteger r = (new BigInteger(randbits, random));
		return sign(document, r);
	}

	private SignatureShare sign(BigInteger document, BigInteger r) throws NoSuchAlgorithmException {

        long initTime = System.currentTimeMillis();

		BigInteger groupVerifier = publicKey.getGroupVerifier();
		BigInteger shareVerifier = publicKey.getShareVerifier(id);
		
		final BigInteger n = publicKey.getModulus();
		
		final BigInteger x = document.mod(n);

		final BigInteger v_prime = groupVerifier.modPow(r, n);
		final BigInteger x_tilde = x.modPow(ThreshUtil.FOUR.multiply(delta), n);
		final BigInteger x_prime = x_tilde.modPow(r, n);

		BigInteger c = null, z = null;

        final BigInteger xi = x.modPow(ThreshUtil.TWO.multiply(delta).multiply(secretShare), n);

		// Try to generate C and Z
		synchronized (lockMd) {
			md = MessageDigest.getInstance("SHA");
			md.reset();

			md.update(groupVerifier.mod(n).toByteArray());
			md.update(x_tilde.toByteArray());
			md.update(shareVerifier.mod(n).toByteArray());
			md.update(xi.modPow(ThreshUtil.TWO, n).toByteArray());
			md.update(v_prime.toByteArray());
			md.update(x_prime.toByteArray());

			c = new BigInteger(md.digest()).mod(n);
		}

		z = (c.multiply(secretShare)).add(r);
		//BigInteger signature = x.modPow(e_prime, n);
        BigInteger signature = new BigInteger(xi.toByteArray()); // TODO: clean this.

        long endTime = System.currentTimeMillis();
        TSLogger.node.info("Took " + (endTime - initTime) + "ms to complete the signature.");

		return new SignatureShare(signature, c, z);
	}

}
