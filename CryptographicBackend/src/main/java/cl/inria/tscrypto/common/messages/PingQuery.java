package cl.inria.tscrypto.common.messages;

import cl.inria.tscrypto.common.datatypes.Ticket;

public class PingQuery extends MessageAsync{

	private String ping_type;
	
	public PingQuery(Ticket ticket, String pingType, String replyTo) {
		super("ping-query,", "1.0", ticket, replyTo);

		this.ping_type = pingType;
	}

	public String getPingType(){
		return this.ping_type;
	}
	
}
