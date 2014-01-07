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

public class SignatureShare {

	public BigInteger signature;
	public BigInteger c;
	public BigInteger z;
	
	/**
	 * no-args constructor required by Gson
	 */
	public SignatureShare() {
	}
	
	public SignatureShare(BigInteger signature, BigInteger c, BigInteger z) {
		this.signature = signature;
		this.c = c;
		this.z = z;
	}

}
