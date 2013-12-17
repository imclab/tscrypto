package cl.inria.tscrypto.common.datatypes;

import java.util.UUID;

public class Ticket {

	private String id;

	private Ticket() {
		this.id = UUID.randomUUID().toString();
	}
	
	public synchronized static Ticket getNextTicket() {
		return new Ticket();
	}
	
	public String getId() {
		return id;
	}

}
