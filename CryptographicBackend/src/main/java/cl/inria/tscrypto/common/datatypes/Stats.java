package cl.inria.tscrypto.common.datatypes;

import cl.inria.tscrypto.common.utils.TSLogger;

public class Stats {
	private enum ShareStatus { UNKNOWN, VALID, INVALID }
	
	ShareStatus[] shareStatus;

	int validSignatureShares;
	int invalidSignatureShares;
	
	int k, l;

	long initialTime;
	long finalTime;
	
	boolean done = false;

	public Stats(KeyMetaInfo keyMetaInfo) {
		k = keyMetaInfo.getK();
		l = keyMetaInfo.getL();
		
		shareStatus = new ShareStatus[l];
		for (int i = 0; i < l; i++) {
			shareStatus[i] = ShareStatus.UNKNOWN;
		}
		
		validSignatureShares = 0;
		invalidSignatureShares = 0;
		
		initialTime = System.currentTimeMillis();
		finalTime = 0;
	}
		
	public int getValidSignatureShares() {
		return validSignatureShares;
	}

	public void finished() {
		finalTime = System.currentTimeMillis();
		done = true;
		TSLogger.sd.info("Finished TS-signature creation in "
				+ (finalTime - initialTime)
				+ "ms");
	}

	public void markInvalid(int id) {
		TSLogger.sd.info("SharesNotOk: | Node: " + id);
		if (shareStatus[id] != ShareStatus.INVALID) {
			shareStatus[id] = ShareStatus.INVALID;
			invalidSignatureShares++;
		}
	}

	public void markValid(int id) {
		TSLogger.sd.info("SharesOk: | Node: " + id);
		if (shareStatus[id] != ShareStatus.VALID) {
			shareStatus[id] = ShareStatus.VALID;
			validSignatureShares++;
		}
	}

	public boolean isValid(int id) {
		return (shareStatus[id] == ShareStatus.VALID);
	}

	public long getInitialTime() {
		return initialTime;
	}

	public long getFinalTime() {
		return finalTime;
	}
	
	public int[] getFirstKValidNodes() {
		int[] validIds = new int[k];
		int pos = 0;
		for (int id = 0; id < l; id++) {
			if (isValid(id)) {
				validIds[pos] = id;
				pos++;
			}
		}
		return validIds;
	}

	public boolean hasEnoughValidShares() {
		return (validSignatureShares >= k);
	}
	
	public boolean isDone() {
		return done;
	}

}
