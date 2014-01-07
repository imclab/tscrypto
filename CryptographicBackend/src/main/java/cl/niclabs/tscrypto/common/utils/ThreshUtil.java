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
import java.security.SecureRandom;

public class ThreshUtil {
	// Constants and variables
	// ............................................................................
	public final static BigInteger ZERO = BigInteger.ZERO;

	public final static BigInteger ONE = BigInteger.ONE;

	public final static BigInteger TWO = BigInteger.valueOf(2L);

	public final static BigInteger FOUR = BigInteger.valueOf(4L);

	/** Fermat prime F4. */
	public final static BigInteger F4 = BigInteger.valueOf(0x10001L);

	/** An arbitrary security parameter for generating secret shares */
	public final static int L1 = 128;

	private static final SecureRandom random = new SecureRandom();

	public static SecureRandom getRandom() {
		return random;
	}
	
	public static BigInteger[] extendedEuclidean(BigInteger p, BigInteger q) {
		if (q.intValue() == 0)
			return new BigInteger[] { p, BigInteger.valueOf(1),
					BigInteger.valueOf(0) };

		BigInteger[] vals = extendedEuclidean(q, p.remainder(q));
		BigInteger d = vals[0];
		BigInteger a = vals[2];
		BigInteger b = vals[1].subtract(p.divide(q).multiply(vals[2]));
		return new BigInteger[] { d, a, b };
	}
	
	/**
	 * Returns the factorial of the given integer as a BigInteger
	 * 
	 * @return l!
	 */
	public static BigInteger factorial(final int l) {
		BigInteger x = BigInteger.valueOf(1l);
		for (int i = 1; i <= l; i++)
			x = x.multiply(BigInteger.valueOf(i));

		return x;
	}

}
