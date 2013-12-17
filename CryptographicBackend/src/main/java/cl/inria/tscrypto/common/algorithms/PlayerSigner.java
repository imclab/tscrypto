package cl.inria.tscrypto.common.algorithms;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

import cl.inria.tscrypto.common.datatypes.SignatureShare;

/**
 * Player capable of creating a signature share in the TS-RSA system
 * @author acansado
 *
 */
public interface PlayerSigner {

	/** Gets the player ID */
	public int getId();

	/**
	 * Signs data and returns the signature share
	 * that can be validated
	 * @param document data to be signed (hashed document mod n)
	 * @param shareVerifier share verifier (VKi)
	 * @param groupVerifier group verifier (VK)
	 * @return signature share
	 * @throws NoSuchAlgorithmException
	 */
	public SignatureShare sign(BigInteger document)
			throws NoSuchAlgorithmException;

}