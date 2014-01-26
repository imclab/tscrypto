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

package cl.niclabs.tscrypto.common.algorithms.dealer;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cl.niclabs.tscrypto.common.datatypes.KeyMetaInfo;
import cl.niclabs.tscrypto.common.datatypes.SignatureShare;
import cl.niclabs.tscrypto.common.datatypes.TSPublicKey;
import cl.niclabs.tscrypto.common.utils.ThreshUtil;

public class SignatureDealerImpl implements SignatureDealer {
    private static final BigInteger TWO = ThreshUtil.TWO;
    private static final BigInteger FOUR = ThreshUtil.FOUR;

	/** meta information about the TS-RSA key used */
	private final KeyMetaInfo keyMetaInfo;
	
	/** TS-RSA public key */
	private final TSPublicKey publicKey;
	
	/** lock for Message Digest -- needed because it is not thread-safe */
	private static Object lockMd = new Object();
	
	// cached computations
	private BigInteger w, eprime, x, delta;
	private BigInteger xtilde;

	private SignatureRequest request;

    public SignatureDealerImpl(KeyMetaInfo keyMetaInfo, TSPublicKey publicKey) {
        this.keyMetaInfo = keyMetaInfo;
        this.publicKey = publicKey;
    }
	
	/* (non-Javadoc)
	 * @see SignatureDealer#prepareSignature(byte[], java.lang.String)
	 */
	@Override
	public SignatureRequest prepareSignature(byte[] document, String hashAlgorithm) throws NoSuchAlgorithmException {
		reset();
		
		request = new SignatureRequest(keyMetaInfo, publicKey);
		request.setDocument(document, hashAlgorithm);
		generateX();
		generateDelta();
		xtilde = x.modPow(BigInteger.valueOf(4l).multiply(delta), publicKey.n);

		return request;
	}
	
	/**
	 * Cleans-up the player's state, except the key.
	 * This must be called before signing another document
	 */
	private void reset() {
		request = new SignatureRequest(keyMetaInfo, publicKey);

		w = eprime = x = delta = null;
		xtilde = null;
	}


	/* (non-Javadoc)
	 * @see SignatureDealer#receiveSignatureShare(SignatureShare, int)
	 */
	@Override
	public void joinSignatureShare(SignatureShare signature, int id) throws NoSuchAlgorithmException {
		boolean isValidSignature = verifySignatureShare(signature, id);

		request.signatureShareReceived(signature, id, isValidSignature);
		
		if (request.hasPendingSignature()) {
			request.setSignature(generateSignature());
		}
	}

	/** Combined signature shares into a signature.
	 */
	private BigInteger generateSignature() {

		// e'a + eb=1
		generateEprime();
		final BigInteger[] vals = ThreshUtil.extendedEuclidean(publicKey.e, eprime);
		final BigInteger a = vals[2], b = vals[1];
		// remove x1, x2, retest?
		// final BigInteger x1 = eprime.multiply(a);
		// final BigInteger x2 = e.multiply(b);
		// final BigInteger retest = x1.add(x2).mod(n);

		// // y = w^a*x^b

		generateW();
		final BigInteger wa = w.modPow(a, publicKey.n);
		final BigInteger xb = x.modPow(b, publicKey.n);
		return wa.multiply(xb).mod(publicKey.n);
	}

	/* (non-Javadoc)
	 * @see SignatureDealer#isSignatureValid()
	 */
	@Override
	public boolean isSignatureValid() {
		// Global Verification
		// Needed information:
		//   - data, x, n (generateX)
		//   - delta (generateEprime)
		//   - correctIds

		if (!request.isReady()) {
			return false;
		}
		
		generateEprime();
		generateW();

		final BigInteger xeprime = x.modPow(eprime, publicKey.n);
		final BigInteger we = w.modPow(publicKey.e, publicKey.n);

		return (xeprime.compareTo(we) == 0);
	}

	/**
	 * Checks if the signature share is valid
	 * @param id player id
	 * @throws NoSuchAlgorithmException hash digest cannot be found
	 */
	private boolean verifySignatureShare(SignatureShare signatureShare, int id) throws NoSuchAlgorithmException {

		final BigInteger TWO = BigInteger.valueOf(2l);

		// Before(vk share vkg group)
		// After (vi share v gkroup)true
        BigInteger v = publicKey.getGroupVerifier();
        BigInteger vi = publicKey.getShareVerifier(id);
		BigInteger c = signatureShare.c;
		BigInteger z = signatureShare.z;
		final BigInteger xi = signatureShare.signature;

		final BigInteger vz = v.modPow(z, publicKey.n);
		final BigInteger vinegc = vi.modPow(c, publicKey.n).modInverse(publicKey.n);

        final BigInteger xineg2c = xi
                .modPow(TWO, publicKey.n)
                .modPow(c, publicKey.n)
                .modInverse(publicKey.n);

		BigInteger result = null;
		// Message Digest is not thread-safe
		synchronized (lockMd) {
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.reset();

			// debug("v :" + v);
			md.update(v.toByteArray());

			// debug("xtilde :" + xtilde);
			md.update(xtilde.toByteArray());

			// debug("vi :" + vi);
			md.update(vi.toByteArray());

			// debug("xi^2 :" + xi.modPow(TWO,n));
			md.update(xi.modPow(TWO, publicKey.n).toByteArray());

			// debug("v^z*v^-c :" + vz.multiply(vinegc).mod(n));
			md.update(vz.multiply(vinegc).mod(publicKey.n).toByteArray());

			final BigInteger xtildez = xtilde.modPow(z, publicKey.n);

			// debug("xi^-2cx: " + xineg2c.multiply(xtildez).mod(n));
			md.update(xineg2c.multiply(xtildez).mod(publicKey.n).toByteArray());
			result = new BigInteger(md.digest()).mod(publicKey.n);
		}
		
		return result.equals(c);
	}

	// Generate necessary values
	// *****************************************************************
	private synchronized void generateX() {
		if (x == null) {
			x = request.getHashedDocument().mod(publicKey.n);
		}
	}

	private synchronized void generateEprime() {
		if (eprime == null) {
            eprime = FOUR.multiply(delta.modPow(TWO, publicKey.n)).mod(publicKey.n);
		}
	}

	private synchronized void generateDelta() {
		if (delta == null) {
			delta = ThreshUtil.factorial(keyMetaInfo.getL());
		}
	}

	private synchronized void generateW() {
		if (w == null) {
			// Now join to verify
			w = BigInteger.valueOf(1l);

			// We need only k nodes to calculate lambda, no more (no less)
			int[] validIds = request.getStats().getFirstKValidNodes();
			
			for (int i = 0; i < keyMetaInfo.getK(); i++) {
				int id = validIds[i];
                BigInteger lambda2 = TWO.multiply(lambda(id + 1, validIds, delta));
				w = w.multiply(request.getSignatureShare(id).signature.modPow(lambda2, publicKey.n));
			}

			w = w.mod(publicKey.n);
		}
	}
	
	/*
	 * Compute Lagrange interpolation points Reference: Shoup, pg 7.
	 * 
	 * @param p
	 *            - a polynomial to evaluate these points on
	 * @param ik
	 *            - a point in S
	 * @param S
	 *            - a set of k points in {0...l}
	 * @param delta
	 *            - the factorial of the group size
	 * 
	 * @return the Lagrange interpolation of these points at 0
	 */
	private static BigInteger lambda(final int ik, int[] ids,
			final BigInteger delta) {
		// lambda(id,l) = PI {id!=j, 0<j<=l} (i-j')/(id-j')
		BigInteger value = delta;

		for (int i = 0; i < ids.length; i++) {
			int id = ids[i] + 1;
			if (id != ik)
				value = value.multiply(BigInteger.valueOf(id));
		}

		for (int i = 0; i < ids.length; i++) {
			int id = ids[i] + 1;
			if (id != ik)
				value = value.divide(BigInteger.valueOf((id - ik)));
		}

		return value;
	}

	@Override
	public SignatureRequest getRequest() {
		return request;
	}

	@Override
	public BigInteger getSignature() {
		return request.getSignature();
	}

	@Override
	public boolean isDone() {
		return (request.getSignature() != null);
	}

}
