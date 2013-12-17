package cl.inria.tscrypto.common.datatypes;

import java.math.BigInteger;

public class KeyShareInfo extends JsonFormat {
    private KeyMetaInfo keyMetaInfo;
    private TSPublicKey publicKey;
    private BigInteger share;

    public KeyShareInfo() {} // Needed by GSON.

    public KeyShareInfo(KeyMetaInfo keyMetaInfo, TSPublicKey publicKey, BigInteger share) {
        this.keyMetaInfo = keyMetaInfo;
        this.publicKey = publicKey;
        this.share = share;
    }

    public KeyMetaInfo getKeyMetaInfo() {
        return keyMetaInfo;
    }

    public TSPublicKey getPublicKey() {
        return publicKey;
    }

    public BigInteger getShare() {
        return share;
    }
}
