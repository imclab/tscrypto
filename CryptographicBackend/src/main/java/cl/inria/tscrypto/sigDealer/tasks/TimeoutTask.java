package cl.inria.tscrypto.sigDealer.tasks;

import java.util.TimerTask;

import cl.inria.tscrypto.common.algorithms.SignatureDealer;

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
