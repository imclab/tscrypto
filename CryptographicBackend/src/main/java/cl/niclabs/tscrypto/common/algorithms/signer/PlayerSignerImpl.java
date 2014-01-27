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

package cl.niclabs.tscrypto.common.algorithms.signer;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import cl.niclabs.tscrypto.common.datatypes.KeyShareInfo;
import cl.niclabs.tscrypto.common.datatypes.SignatureShare;
import cl.niclabs.tscrypto.common.utils.TSLogger;

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
public class PlayerSignerImpl extends AbstractPlayerSigner {

    /**
     * Player signer in charge of a keyShare
     * @param key key used by this player
     * @param id player id
     */
	public PlayerSignerImpl(KeyShareInfo key, int id) {
		super (key, id);
	}


	protected SignatureShare sign(BigInteger document, BigInteger r) throws NoSuchAlgorithmException {

        long initTime = System.currentTimeMillis();
		BigInteger groupVerifier = publicKey.getGroupVerifier();
		BigInteger shareVerifier = publicKey.getShareVerifier(id);
		
		final BigInteger n = publicKey.getModulus();

        final BigInteger x = document.mod(n);
        final BigInteger xi = x.modPow(TWO.multiply(delta).multiply(secretShare), n);
        final BigInteger xi2n = xi.modPow(TWO, n);
        final BigInteger v_prime = groupVerifier.modPow(r, n);
        final BigInteger x_tilde = x.modPow(FOUR.multiply(delta), n);
        final BigInteger x_prime = x_tilde.modPow(r, n);


        BigInteger c, z;

        // Try to generate C and Z
        synchronized (lockMd) {
            md = MessageDigest.getInstance("SHA");
            md.reset();

            md.update(groupVerifier.mod(n).toByteArray());
            md.update(x_tilde.toByteArray());
            md.update(shareVerifier.mod(n).toByteArray());

			md.update(xi2n.toByteArray());

			md.update(v_prime.toByteArray());
			md.update(x_prime.toByteArray());

			c = new BigInteger(md.digest()).mod(n);
		}

		z = (c.multiply(secretShare)) .add(r);
        long endTime = System.currentTimeMillis();
        TSLogger.node.info("Took " + (endTime - initTime) + "ms to complete the signature.");

        BigInteger signature = xi;
        return new SignatureShare(signature, c, z);
	}

}
