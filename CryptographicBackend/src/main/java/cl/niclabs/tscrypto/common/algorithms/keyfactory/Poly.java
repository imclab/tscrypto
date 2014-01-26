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

package cl.niclabs.tscrypto.common.algorithms.keyfactory;

import java.math.BigInteger;

import cl.niclabs.tscrypto.common.utils.ThreshUtil;


class Poly {
	private BigInteger[] coeff;

	private int size;

	/**
	 * Generates a polynomial, f(x) mod m such that f(0) = d<BR>
	 * All coefficients are chosen randomly from Z(m) (modulo m)
	 * 
	 * @param d
	 *            - The first coeffecient of the polynomial
	 * @param size
	 *            - The size of the polynomial
	 * @param m
	 *            - modulo of field coeffiecients are from
	 */

	public Poly(final BigInteger d, final int size, final BigInteger m) {
		final int bitLength = m.bitLength();

		this.size = size;
		coeff = new BigInteger[size];
		coeff[0] = d;
		for (int i = 1; i < size; i++)
			coeff[i] = (new BigInteger(bitLength, ThreshUtil.getRandom()))
					.mod(m);
	}

	public BigInteger eval(final BigInteger x) {
		BigInteger retVal = coeff[size - 1];

		for (int i = size - 2; i >= 0; i--)
			// debug(retVal.toString(10));
			retVal = (retVal.multiply(x)).add(coeff[i]);
		return retVal;
	}

	public BigInteger eval(final int x) {
		final BigInteger bx = BigInteger.valueOf(x);
		return this.eval(bx);
	}

	@Override
	public String toString() {
		String out = "[";
		for (int i = 0; i < this.size; i++)
			out += coeff[i].toString() + " ";
		out += "]";
		return out;
	}
}
