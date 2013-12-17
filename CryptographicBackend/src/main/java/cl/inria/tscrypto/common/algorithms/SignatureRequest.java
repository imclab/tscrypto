package cl.inria.tscrypto.common.algorithms;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cl.inria.tscrypto.common.datatypes.KeyMetaInfo;
import cl.inria.tscrypto.common.datatypes.SignatureShare;
import cl.inria.tscrypto.common.datatypes.Stats;
import cl.inria.tscrypto.common.datatypes.TSPublicKey;
import cl.inria.tscrypto.common.utils.Encoder;
import cl.inria.tscrypto.common.utils.TSLogger;

public class SignatureRequest {

	private BigInteger hashedDocument;

	// signatures
	/** combined signature -- may never be available if there are insufficient shares */
	private BigInteger signature;
	/** signature share sent by players */
	private SignatureShare[] signatureShares;
	
	/** statistics of the signing process */
	private Stats stats;
	/** lock for stats */
	private Object lockStats = new Object();

	private KeyMetaInfo keyMetaInfo;

	private TSPublicKey publicKey;

	public SignatureRequest(KeyMetaInfo keyMetaInfo, TSPublicKey publicKey) {
		this.keyMetaInfo = keyMetaInfo;
		this.publicKey = publicKey;
        signatureShares = new SignatureShare[keyMetaInfo.getL()];
		stats = new Stats(keyMetaInfo);
	}

	public BigInteger getSignature() {
		return signature;
	}

	public byte[] getSignatureAsByte() {
		
		byte aux[] = signature.toByteArray();
		int size = (int) Math.ceil(publicKey.n.bitLength() / 8);
		byte[] byteSignature = new byte[size];
		System.arraycopy(aux, aux.length - size, byteSignature, 0, size);
		
		return byteSignature;
	}
	
	/** Hashes the document using the specified hash algorithm.
	 * Currently only "NONE" and "Sha1" are supported
	 * @param blob data to be hashed
	 * @param hashAlgorithm hash algorithm to be used
	 * @return hashed document
	 * @throws NoSuchAlgorithmException hash algorithm couldn't be found
	 */
	private BigInteger hashData(byte[] blob, String hashAlgorithm) throws NoSuchAlgorithmException {
		BigInteger data;
		int size = keyMetaInfo.getKeysize() / 4;

        switch (hashAlgorithm) {
            case "NONE":
                data = new BigInteger(1, Encoder.encodeNoHash(blob,
                        size));
                break;
            case "Sha1":
                MessageDigest dig = MessageDigest.getInstance("SHA-1");
                byte hdata[] = dig.digest(blob);
                data = new BigInteger(1, Encoder.encodeSha1(hdata, size));
                break;
            default:
                throw new NoSuchAlgorithmException(
                        "No algorithm with name:" + hashAlgorithm
                );
        }
		
		return data;
	}

	public void setDocument(byte[] document, String hashAlgorithm) throws NoSuchAlgorithmException {
		hashedDocument = hashData(document, hashAlgorithm);
	}

	public BigInteger getHashedDocument() {
		return hashedDocument;
	}

	public void signatureShareReceived(SignatureShare signatureShare, int id, boolean isValidSignature) {
		signatureShares[id] = signatureShare;
		synchronized (lockStats) {
			if (isValidSignature) {
				stats.markValid(id);
			} else {
				stats.markInvalid(id);
			}
		}


	}

	public SignatureShare getSignatureShare(int id) {
		return signatureShares[id];
	}

	public Stats getStats() {
		return stats;
	}

	public boolean hasPendingSignature() {
		if (stats.hasEnoughValidShares() && !stats.isDone()) {
			return true;
		}

		return false;
	}

	public synchronized void setSignature(BigInteger signature) {
		this.signature = signature;
		stats.finished();
        // With this, we notify all who is waiting for signature that's ready...
        this.notifyAll();
	}

    public void waitUntilReady() throws InterruptedException {
        while(signature == null) {
            synchronized (this) {
                while(signature == null) {
                    TSLogger.sd.info("Waiting for signature share.");
                    wait();
                }
            }
        }
    }

	public boolean isReady() {
        return stats.getValidSignatureShares() >= keyMetaInfo.getK();
	}


}
