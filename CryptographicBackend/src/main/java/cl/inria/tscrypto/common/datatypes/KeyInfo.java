package cl.inria.tscrypto.common.datatypes;


public class KeyInfo extends JsonFormat {

	private KeyMetaInfo keyMetaInfo;

	private TSPublicKey publicKey;
	private KeyShares keyShares;

	/**
	 * Empty no-args constructor needed by Gson
	 */
	public KeyInfo() {}

	public KeyInfo(KeyMetaInfo keyMetaInfo, TSPublicKey publicKey) {
		this.keyMetaInfo = keyMetaInfo;
		this.publicKey = publicKey;
	}

	public KeyInfo(KeyMetaInfo keyMetaInfo, TSPublicKey publicKey, KeyShares keyShares) {
		this.keyMetaInfo = keyMetaInfo;
		this.publicKey = publicKey;
		this.keyShares = keyShares;
	}

	public TSPublicKey getPublicKey() {
		return publicKey;
	}
	
	public KeyMetaInfo getKeyMetaInfo() {
		return keyMetaInfo;
	}

	public KeyShares getKeyShares() {
		return keyShares;
	}
    
	public void setKeyShares(KeyShares keyShares) {
		this.keyShares = keyShares;
	}
	
}
