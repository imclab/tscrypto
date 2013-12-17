package cl.inria.tscrypto.sigDealer.handlers;

import java.security.NoSuchAlgorithmException;

import cl.inria.tscrypto.common.algorithms.SignatureDealer;
import cl.inria.tscrypto.common.messages.SignShareAnswer;
import cl.inria.tscrypto.common.messages.TSMessage;
import cl.inria.tscrypto.common.utils.TSLogger;
import cl.inria.tscrypto.sigDealer.RequestManager;

public class SignedShareHandler implements SDHandler {

	private SignShareAnswer message;
	
	@Override
	public void init(TSMessage message) {
		this.message = (SignShareAnswer) message;
	}

	
	@Override
	public void handle(RequestManager manager) {
		SignatureDealer request = manager.getSigningRequest(message.ticket);
		if (request == null) {
			TSLogger.sd.info("Could not find signing request " + message.ticket);
			return;
		}
		
		if (request.isDone()) {
			TSLogger.sd.info("Signature Share will not used: | Node: " + message.node_id + "| Ticket: "
					+ message.ticket.getId());
			return;
		}

		TSLogger.sd.info("Signature Share will be used: | Node: " + message.node_id + "| Ticket: "
				+ message.ticket.getId());

		try {
			request.joinSignatureShare(message.signature, message.node_id);
		} catch (NoSuchAlgorithmException e) {
			TSLogger.sd.error("Could not find hash algorithm:" + e.getMessage());
		}

	}
}
