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

import org.junit.Assert;
import org.junit.Test;

import cl.niclabs.tscrypto.common.datatypes.KeyMetaInfo;
import cl.niclabs.tscrypto.common.utils.Util;


public class KeyMetaInfoTest {
	
	@Test
    public void verify_constructor() {
    	int k = 4;
    	int l = 5;
    	int keysize = 1024;
    	KeyMetaInfo keyMetaInfo = new KeyMetaInfo(k, l, keysize);

    	checkKeyMetaInfo(k, l, keysize, keyMetaInfo);
    }
    
    @Test
    public void verify_Json_constructor() {
    	String json = "{ \"k\": 4, \"l\": 7, \"keysize\": 512, \"alias\": \"eb133b85-e61e-4d27-baef-c96cc89abdcd\", \"algorithm\": \"RSA\"}";
    	
    	KeyMetaInfo keyMetaInfo = Util.GsonFactory(true).fromJson(json, KeyMetaInfo.class);
    	checkKeyMetaInfo(4, 7, 512, keyMetaInfo);
    }
    
    private void checkKeyMetaInfo(int k, int l, int keysize, KeyMetaInfo keyMetaInfo) {
    	Assert.assertEquals(keyMetaInfo.getK(), k);
        Assert.assertEquals(keyMetaInfo.getL(), l);
        Assert.assertEquals(keyMetaInfo.getKeysize(), keysize);
        Assert.assertEquals(keyMetaInfo.getAlgorithm(), "RSA");
    }
}
