package cl.inria.tscrypto.common.messages;

import cl.inria.tscrypto.common.datatypes.Ticket;

public abstract class MessageAsync extends TSMessage {

	public Ticket ticket;
	private String replyTo;

	public MessageAsync() {
	}
	
	protected MessageAsync(String type, String version, Ticket ticket, String replyTo) {
		super(type, version);
		
		this.ticket = ticket;
		this.replyTo = replyTo;
	}
	
	public String getReplyTo() {
		return this.replyTo;
	}

}
