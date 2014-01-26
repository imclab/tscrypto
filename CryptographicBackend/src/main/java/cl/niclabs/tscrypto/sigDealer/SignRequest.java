package cl.niclabs.tscrypto.sigDealer;

import cl.niclabs.tscrypto.common.algorithms.dealer.SignatureDealerImpl;
import cl.niclabs.tscrypto.common.algorithms.dealer.SignatureRequest;
import cl.niclabs.tscrypto.common.datatypes.KeyMetaInfo;
import cl.niclabs.tscrypto.common.datatypes.TSPublicKey;

import java.security.NoSuchAlgorithmException;

public class SignRequest implements Request {
    private final SignatureDealerImpl signatureDealer;
    private final SignatureRequest signatureRequest;

    public SignRequest(KeyMetaInfo keyMetaInfo, TSPublicKey publicKey, byte[] blob, String hashAlgorithm) throws NoSuchAlgorithmException {
        signatureDealer = new SignatureDealerImpl(keyMetaInfo, publicKey);
        signatureRequest = signatureDealer.prepareSignature(blob, hashAlgorithm);
    }

    @Override
    public void setReady(int nodeId) {
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void waitUntilReady() throws InterruptedException {

    }
}
