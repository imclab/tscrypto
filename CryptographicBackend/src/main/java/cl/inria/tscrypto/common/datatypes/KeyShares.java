package cl.inria.tscrypto.common.datatypes;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;



public class KeyShares extends JsonFormat {
	private BigInteger[] secrets;

	/**
	 * Empty no-args constructor needed by Gson
	 */
	public KeyShares() {
	}
	
	public KeyShares(KeyMetaInfo keyMetaInfo) {
		this.secrets = new BigInteger[keyMetaInfo.getL()];
	}

	public KeyShares(KeyShares keyShares, int id) {
		this.secrets = new BigInteger[keyShares.secrets.length];
		setSecret(id, keyShares.getSecret(id));
	}
	
	public List<String> getSecrets() {
		List<String> listSecrets = new LinkedList<String>();
		for (BigInteger secret : secrets) {
			listSecrets.add(secret.toString());
		}
		return listSecrets;
	}
	
	public void setSecret(int i, BigInteger secret) {
		this.secrets[i] = secret;
	}

	public BigInteger getSecret(int i) {
		return secrets[i];
	}

}
