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


public class KeyInfo extends JsonFormat {

	private KeyMetaInfo keyMetaInfo;

	private TSPublicKey publicKey;
	private KeyShares keyShares;

	/**
	 * Empty no-args constructor needed by Gson
	 */
	public KeyInfo() {}

	public KeyInfo(KeyMetaInfo keyMetaInfo, TSPublicKey publicKey) {
		this.keyMetaInfo = keyMetaInfo;
		this.publicKey = publicKey;
	}

	public KeyInfo(KeyMetaInfo keyMetaInfo, TSPublicKey publicKey, KeyShares keyShares) {
		this.keyMetaInfo = keyMetaInfo;
		this.publicKey = publicKey;
		this.keyShares = keyShares;
	}

	public TSPublicKey getPublicKey() {
		return publicKey;
	}
	
	public KeyMetaInfo getKeyMetaInfo() {
		return keyMetaInfo;
	}

	public KeyShares getKeyShares() {
		return keyShares;
	}
    
	public void setKeyShares(KeyShares keyShares) {
		this.keyShares = keyShares;
	}
	
}
