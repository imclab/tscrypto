package cl.inria.tscrypto.testing.datatypes;

import java.math.BigInteger;

import org.junit.Ignore;
import org.junit.Test;

import cl.inria.tscrypto.common.datatypes.KeyMetaInfo;
import cl.inria.tscrypto.common.utils.Util;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
