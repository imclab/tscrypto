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

package cl.niclabs.tscrypto.testing.datatypes;

import java.math.BigInteger;

import org.junit.Test;

import cl.niclabs.tscrypto.common.utils.Util;
import static org.junit.Assert.*;

public class BigIntegerBase64TypeAdapterTest {

	@Test
	public void test_serialization() {
		
		BigInteger number = new BigInteger("123456789");
		
		// serialization
		String json = Util.GsonFactory(true).toJson(number);

		// de-serialization
		BigInteger number2 = Util.GsonFactory(true).fromJson(json, BigInteger.class);
		
		assertEquals(number, number2);
		
	}
	
	
}
