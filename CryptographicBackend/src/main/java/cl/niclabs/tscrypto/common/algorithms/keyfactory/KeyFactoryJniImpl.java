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

package cl.niclabs.tscrypto.common.algorithms.keyfactory;

import cl.niclabs.tscrypto.common.algorithms.JniSignWrapper;
import cl.niclabs.tscrypto.common.datatypes.KeyInfo;
import cl.niclabs.tscrypto.common.datatypes.KeyMetaInfo;
import cl.niclabs.tscrypto.common.datatypes.KeyShares;
import cl.niclabs.tscrypto.common.datatypes.TSPublicKey;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.common.utils.ThreshUtil;

import java.math.BigInteger;

public class KeyFactoryJniImpl implements KeyFactory {

	/**
	 * Generate a group public key and l shares for a (k,l) <BR>
	 * threshold signatures scheme<BR>
	 * 
	 * @param primesize bit length that the returned strong prime should have
	 * @param k minimum number of honest players
	 * @param l total number of players
	 *
	 */
    @Override
	public KeyInfo generateKeys(int primesize, int k, int l) {

		KeyMetaInfo keyMetaInfo = new KeyMetaInfo(k, l, primesize*2);
		
		BigInteger pr, qr, p, q, d, e;
		BigInteger groupSize;
		BigInteger m = pr = qr = null;

		// Create the group key pair
		TSLogger.keyFactory.info("Attempting to generate group keypair..");

		/* Generate a Sophie Germain prime keypair */
		// pr = generateSophieGermainPrime();
		// qr = generateSophieGermainPrime();
		p = SafePrimeGenImpl.generateStrongPrime(primesize, ThreshUtil.getRandom());
		q = SafePrimeGenImpl.generateStrongPrime(primesize, ThreshUtil.getRandom());

		pr = (p.subtract(ThreshUtil.ONE)).divide(ThreshUtil.TWO);
		qr = (q.subtract(ThreshUtil.ONE)).divide(ThreshUtil.TWO);

		// m = pr*qr
		m = pr.multiply(qr);

		// p = 2*pr + 1
		// p = (pr.multiply(TWO)).add(ONE);

		// q = 2*qr + 1
		// q = (qr.multiply(TWO)).add(ONE);

		// n = p*q
		BigInteger n = p.multiply(q);

		// the RSA public exponent must be a prime bigger than
		// l, the size of the group
		groupSize = BigInteger.valueOf(l);

		// If group size is less than Fermat's prime, just use it.
		if (groupSize.compareTo(ThreshUtil.F4) < 0)
			e = ThreshUtil.F4;
		// Otherwise pick a prime bigger than groupSize
		else
			e = new BigInteger(groupSize.bitLength() + 1, 80, ThreshUtil
					.getRandom());

		// Note: This is not a standard RSA Key Pair
		// Usually:
		// BigInteger phi = (p.subtract(ONE)).multiply(q.subtract(ONE));
		// d = e.modInverse(phi);
		d = e.modInverse(m);

		// Create Secret KeyShares and KeyShare Verifiers
		// Note: We don't use the private exponent 'd' after this
		KeyShares keyShares = generateKeyShares(keyMetaInfo, n, d, m);

		// verifiers
		BigInteger groupVerifier = generateGroupVerifier(keyMetaInfo, n);
		BigInteger[] shareVerifiers = generateShareVerifiers(keyMetaInfo, n, groupVerifier, keyShares);

		TSPublicKey publicKey = new TSPublicKey(keyMetaInfo, e, n, groupVerifier, shareVerifiers);

		KeyInfo keyInfo = new KeyInfo(keyMetaInfo, publicKey, keyShares);
		return keyInfo;
	}

	/**
	 * Creates verifiers for secret shares to prevent corrupting shares<BR>
	 * 
	 * Computes v[i] = v^^s[i] mod n, where v is an element of QR_n <BR>
	 * Returns the group verifier and sets the verifier in each share<br>
	 * 
	 * @param n
	 *            - Size of modulo for group key
	 * @param secrets
	 *            - array of shares
	 * 
	 * @return the group verifier
	 */
	private static BigInteger generateGroupVerifier(KeyMetaInfo keyMetaInfo, BigInteger n) {
		TSLogger.keyFactory.info("Generating Group Verifier");		

		BigInteger rand = null;

		// v is an element of Q*n (squares of relative primes mod n)
		while (true) {
			rand = new BigInteger(n.bitLength(), ThreshUtil.getRandom());
			// ensure that rand is relatively prime to n
			BigInteger d = rand.gcd(n);
			if (d.compareTo(ThreshUtil.ONE) == 0)
				break;
			// Else d was not relatively prime
			// Note: This should be very rare
			TSLogger.keyFactory.warn("Verifier was not relatively prime");
		}

		// group verifier is an element of QsubN - square mod n
		return rand.multiply(rand).mod(n);
	}
	
	private static BigInteger[] generateShareVerifiers(KeyMetaInfo keyMetaInfo, BigInteger n, BigInteger groupVerifier, KeyShares keyShares) {
		TSLogger.keyFactory.info("Generating Share Verifiers");

		BigInteger[] verifiers = new BigInteger[keyMetaInfo.getL()];
		for (int i = 0; i < keyMetaInfo.getL(); i++) {
			// v_i = v^{s_i} in Q_n
			verifiers[i] = JniSignWrapper.modPow(groupVerifier, keyShares.getSecret(i), n);
		}
		
		return verifiers;
	}

	// Private Methods
	// ............................................................................
	/**
	 * 
	 * Generates secret shares for a (k,l) threshold signatures scheme<BR>
	 * 
	 * @param k
	 *            - k valid signatures will verify
	 * @param l
	 *            - l members of the group will receive shares
	 * 
	 * @return An array of l secret shares
	 * @throws ThresholdSigException
	 */
	private static KeyShares generateKeyShares(KeyMetaInfo keyMetaInfo, BigInteger n, final BigInteger d,
			final BigInteger m) {
		BigInteger rand;
		int randbits;
		
		KeyShares keyShares = new KeyShares(keyMetaInfo);

		Poly poly = new Poly(d, keyMetaInfo.getK() - 1, m);

		randbits = n.bitLength() + ThreshUtil.L1 - m.bitLength();

		// Generates the values f(i) for 1<=i<=l
		// and add some large multiple of m to each value
		for (int i = 0; i < keyMetaInfo.getL(); i++) {
			keyShares.setSecret(i, poly.eval(i + 1));
			rand = (new BigInteger(randbits, ThreshUtil.getRandom()))
					.multiply(m);
			keyShares.setSecret(i, keyShares.getSecret(i).add(rand));
		}
		
		return keyShares;
	}


}
