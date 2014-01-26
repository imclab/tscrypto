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
import cl.niclabs.tscrypto.common.algorithms.dealer.SignatureDealer;
import cl.niclabs.tscrypto.common.algorithms.dealer.SignatureDealerImpl;
import cl.niclabs.tscrypto.common.algorithms.dealer.SignatureDealerJniImpl;
import cl.niclabs.tscrypto.common.algorithms.dealer.SignatureRequest;
import cl.niclabs.tscrypto.common.algorithms.keyfactory.KeyFactoryJniImpl;
import cl.niclabs.tscrypto.common.algorithms.signer.PlayerSigner;
import cl.niclabs.tscrypto.common.algorithms.signer.PlayerSignerImpl;
import cl.niclabs.tscrypto.common.algorithms.signer.PlayerSignerJniImpl;
import cl.niclabs.tscrypto.common.datatypes.KeyInfo;
import cl.niclabs.tscrypto.common.datatypes.KeyShareInfo;
import cl.niclabs.tscrypto.common.utils.ThreshUtil;
import cl.niclabs.tscrypto.common.algorithms.keyfactory.KeyFactoryImpl;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;


@RunWith(JUnit4.class)
public class AlgorithmTest {

    static byte data[] = "texto de prueba".getBytes();
    static int k = 5;
    static int l = 9;
    static int keysize = 512;


    @Test
    @Ignore
    public void modPowTest() {
        SecureRandom random = ThreshUtil.getRandom();
        for(int i = 0; i < 100; i++) {
            byte[] nBytes = new byte[1024];
            random.nextBytes(nBytes);
            byte[] mBytes = new byte[50];
            random.nextBytes(mBytes);

            BigInteger m = new BigInteger(mBytes).abs();
            BigInteger e = BigInteger.valueOf(random.nextLong()).abs();
            BigInteger n = new BigInteger(nBytes).abs();

            BigInteger res = JniSignWrapper.modPow(m, e, n);
            Assert.assertEquals(m.modPow(e, n), res);
        }
    }

    @Test
    @Ignore
    public void KeySharesFactoryTest() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        KeyInfo keyInfo = new KeyFactoryImpl().generateKeys(keysize, k, l);
        PublicKey pKey = keyInfo.getPublicKey().convertoToPublicKey();

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


        for (int[] set: subsets) {
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

    @Test
    public void JniTest() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        PublicKey pKey;
        KeyInfo keyInfo = new KeyFactoryJniImpl().generateKeys(keysize, k, l);
        pKey = keyInfo.getPublicKey().convertoToPublicKey();

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


        for (int[] set: subsets) {
            SignatureDealer sd = new SignatureDealerJniImpl(keyInfo.getKeyMetaInfo(), keyInfo.getPublicKey());

            SignatureRequest sr = sd.prepareSignature(data, "Sha1");

            for (int i = 0; i < set.length; i++) {
                PlayerSigner ps = new PlayerSignerJniImpl(keyShareInfos[set[i]],set[i]);
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

    @Test
    @Ignore
    public void keySizeTest() throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyInfo keyInfo = new KeyFactoryImpl().generateKeys(keysize / 2, k, l);
        RSAPublicKey pKey = (RSAPublicKey) keyInfo.getPublicKey().convertoToPublicKey();
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

        SignatureDealer sd = new SignatureDealerImpl(keyInfo.getKeyMetaInfo(), keyInfo.getPublicKey());
        SignatureRequest sr = sd.prepareSignature(data, "NONE");
        for (int i=0; i<l; i++) {
            PlayerSigner ps = new PlayerSignerImpl(keyShareInfos[i], i);
            sd.joinSignatureShare(ps.sign(sr.getHashedDocument()), i);
        }

        BigInteger signature = sd.getSignature();
        long length = signature.toByteArray().length;

        System.out.println(length*8);
        Assert.assertTrue(keysize == length*8);
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

