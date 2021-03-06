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

package cl.niclabs.tscrypto.common.utils;

import java.math.BigInteger;

public class Encoder {

	public static byte[] encodeNoHash(byte data[], int len) {
		return encode(data, new byte[] {}, len);
	}

	public static byte[] encodeSha1(byte data[], int len) {

		// DER DigestInfo for SHA1 = 3021300906052b0e03021a05000414

		return encode(data,
				new BigInteger("3021300906052b0e03021a05000414", 16)
						.toByteArray(), len);
	}

	private static byte[] encode(byte data[], byte digestInfo[], int len) {
		byte[] T = new byte[len];

		System.arraycopy(data, 0, T, len - data.length, data.length);
		System.arraycopy(digestInfo, 0, T, len - data.length
				- digestInfo.length, digestInfo.length);
		T[len - data.length - digestInfo.length - 1] = 0;

		for (int i = 2; i < len - data.length - digestInfo.length - 1; i++) {
			T[i] = ~0;
		}

		T[0] = 0;
		T[1] = 1;
		return T;
	}
	
	public static String byteArrayToHexString(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase();
	}

}
