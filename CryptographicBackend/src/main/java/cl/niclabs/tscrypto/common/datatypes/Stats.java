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

package cl.niclabs.tscrypto.common.datatypes;

import cl.niclabs.tscrypto.common.utils.TSLogger;

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
