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
import java.security.NoSuchAlgorithmException;

import cl.niclabs.tscrypto.common.datatypes.SignatureShare;

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