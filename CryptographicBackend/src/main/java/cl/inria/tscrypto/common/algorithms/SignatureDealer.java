package cl.inria.tscrypto.common.algorithms;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

import cl.inria.tscrypto.common.datatypes.SignatureShare;

/**
 * Signature Dealer and Merger for the TS-RSA system.
 * Sets-up the signature process and is the one
 * responsible of joining signature shares into
 * a valid signature (if possible).
 * @author acansado
 *
 */
public interface SignatureDealer {
	/**
	 * Prepare to sign a new document.
	 * Must be called at the beginning of a signing process
	 * @param document original data
	 * @param hashAlgorithm
	 * @return request
	 * @throws NoSuchAlgorithmException
	 */
	SignatureRequest prepareSignature(byte[] document,
			String hashAlgorithm) throws NoSuchAlgorithmException;

	/**
	 * Joins a signature share.
	 * @param signature signature share created by node id
	 * @param id node that created the signature share
	 * @throws NoSuchAlgorithmException
	 */
	void joinSignatureShare(SignatureShare signature, int id)
			throws NoSuchAlgorithmException;

	/**
	 * Is signature valid?
	 * @return true if the signature is valid
	 */
	boolean isSignatureValid();

	SignatureRequest getRequest();

	BigInteger getSignature();
	
	boolean isDone();
	
}