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

package cl.niclabs.tscrypto.sigDealer.tasks;

import java.util.TimerTask;

import cl.niclabs.tscrypto.common.algorithms.dealer.SignatureDealer;

public class TimeoutTask extends TimerTask {

	private SignatureDealer request;

	public TimeoutTask(SignatureDealer request) {
		this.request = request;
	}

	public void run() {

		// TODO missing
		//		if (!request.isDone()) {
//			request.setDone(true);
//			if (request.getValidSignatures() >= sd.getK()) {
//				request.generateSignature();
//				sd.executeSign(request);
//			} else {
//				sd.notifySignatureReady(request.getTicket(),
//								BigInteger.ONE);
//				//One es la marca de error, es raro
//				request.setTimeout(true);
//			}
//		}
	}

}
