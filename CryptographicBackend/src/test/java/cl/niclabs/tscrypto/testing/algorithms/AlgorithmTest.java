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

/**
 * Created with IntelliJ IDEA.
 * User: tbarros
 * Date: 6/17/13
 * Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */

package cl.niclabs.tscrypto.testing.algorithms;

import cl.niclabs.tscrypto.common.algorithms.*;
import cl.niclabs.tscrypto.common.datatypes.KeyInfo;
import cl.niclabs.tscrypto.common.datatypes.KeyShareInfo;
import cl.niclabs.tscrypto.keyFactory.algorithm.KeyFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;


@RunWith(JUnit4.class)
public class AlgorithmTest {

    static byte data[] = "texto de prueba".getBytes();
    static int k = 5;
    static int l = 9;
    static int keysize = 512;

    PublicKey pKey;

    @Test
    public void KeySharesFactoryTest() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {

        KeyInfo keyInfo = KeyFactory.generateKeys(keysize, k, l);
        this.pKey = keyInfo.getPublicKey().convertoToPublicKey();

        int[] superset = new int[l];
        KeyShareInfo[] keyShareInfos = new KeyShareInfo[l];

        for (int i = 0; i < superset.length; i++) {
            superset[i] = i;
            keyShareInfos[i] = new KeyShareInfo(
                    keyInfo.getKeyMetaInfo(),
                    keyInfo.getPublicKey(),
                    keyInfo.getKeyShares().getSecret(i)
            );
        }

        List<int[]> subsets = processSubsets(superset,k);


        for (int[] set:subsets) {
            SignatureDealer sd = new SignatureDealerImpl(keyInfo.getKeyMetaInfo(), keyInfo.getPublicKey());

            SignatureRequest sr = sd.prepareSignature(data, "Sha1");

            for (int i = 0; i < set.length; i++) {
                PlayerSigner ps = new PlayerSignerImpl(keyShareInfos[set[i]],set[i]);
                BigInteger hash = sr.getHashedDocument();
                sd.joinSignatureShare(ps.sign(hash), set[i]);
            }

            BigInteger signature = sd.getSignature();

            Signature sign = Signature.getInstance("Sha1withRSA");
            sign.initVerify(pKey);
            sign.update(data);

            Assert.assertTrue(sign.verify(signature.toByteArray()));
        }
    }


    static List<int[]> processSubsets(int[] set, int k) {
        List<int[]> result = new ArrayList<int[]>();
        int[] subset = new int[k];
        processLargerSubsets(set, subset, 0, 0,result);
        return result;
    }

    static void processLargerSubsets(int[] set, int[] subset, int subsetSize, int nextIndex, List<int[]> result) {
        if (subsetSize == subset.length) {
            result.add(subset.clone());
        } else {
            for (int j = nextIndex; j < set.length; j++) {
                subset[subsetSize] = set[j];
                processLargerSubsets(set, subset, subsetSize + 1, j + 1,result);
            }
        }
    }


}

