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