package cl.niclabs.tscrypto.sigDealer;


import cl.niclabs.tscrypto.common.algorithms.SignatureDealer;
import cl.niclabs.tscrypto.common.algorithms.SignatureDealerImpl;
import cl.niclabs.tscrypto.common.algorithms.SignatureDealerJniImpl;
import cl.niclabs.tscrypto.common.datatypes.KeyMetaInfo;
import cl.niclabs.tscrypto.common.datatypes.TSPublicKey;

public class SignatureDealerFactory {
    public static SignatureDealer getInstance(KeyMetaInfo keyMetaInfo, TSPublicKey publicKey) {
        if(SDConfig.getInstance().getUseJni()) {
            return new SignatureDealerJniImpl(keyMetaInfo, publicKey);
        } else {
            return new SignatureDealerImpl(keyMetaInfo, publicKey);
        }
    }
}
