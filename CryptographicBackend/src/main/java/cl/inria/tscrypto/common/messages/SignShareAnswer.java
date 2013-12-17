package cl.inria.tscrypto.common.messages;

import cl.inria.tscrypto.common.datatypes.SignatureShare;
import cl.inria.tscrypto.common.datatypes.Ticket;


public class SignShareAnswer extends MessageAsync {

	public SignatureShare signature;
	public int node_id;
	
	public SignShareAnswer(Ticket ticket, SignatureShare signature, int node_id, String replyTo) {
		super("signShare-answer", "1.0", ticket, replyTo);
		this.signature = signature;
		this.node_id = node_id;
	}

}
