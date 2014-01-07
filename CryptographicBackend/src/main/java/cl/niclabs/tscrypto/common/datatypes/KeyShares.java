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
import java.util.LinkedList;
import java.util.List;



public class KeyShares extends JsonFormat {
	private BigInteger[] secrets;

	/**
	 * Empty no-args constructor needed by Gson
	 */
	public KeyShares() {
	}
	
	public KeyShares(KeyMetaInfo keyMetaInfo) {
		this.secrets = new BigInteger[keyMetaInfo.getL()];
	}

	public KeyShares(KeyShares keyShares, int id) {
		this.secrets = new BigInteger[keyShares.secrets.length];
		setSecret(id, keyShares.getSecret(id));
	}
	
	public List<String> getSecrets() {
		List<String> listSecrets = new LinkedList<String>();
		for (BigInteger secret : secrets) {
			listSecrets.add(secret.toString());
		}
		return listSecrets;
	}
	
	public void setSecret(int i, BigInteger secret) {
		this.secrets[i] = secret;
	}

	public BigInteger getSecret(int i) {
		return secrets[i];
	}

}
