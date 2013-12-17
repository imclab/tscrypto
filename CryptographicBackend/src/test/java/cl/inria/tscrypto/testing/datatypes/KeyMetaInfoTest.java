package cl.inria.tscrypto.testing.datatypes;

import org.junit.Assert;
import org.junit.Test;

import cl.inria.tscrypto.common.datatypes.KeyMetaInfo;
import cl.inria.tscrypto.common.utils.Util;

import static org.mockito.Mockito.*;


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
