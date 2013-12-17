package cl.inria.tscrypto.common.messages;

import cl.inria.tscrypto.common.datatypes.Ticket;

public class PingAnswer extends MessageAsync {
	
	public int node_id;
	public String ping_type;

	public PingAnswer(Ticket ticket, int node_id, String ping_type, String replyTo) {
		super("ping-answer,", "1.0", ticket, replyTo);

		this.node_id = node_id;
		this.ping_type = ping_type;
	}

}
