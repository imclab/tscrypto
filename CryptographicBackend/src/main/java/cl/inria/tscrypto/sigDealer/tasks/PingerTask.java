package cl.inria.tscrypto.sigDealer.tasks;

import java.util.TimerTask;

import cl.inria.tscrypto.common.datatypes.Ticket;
import cl.inria.tscrypto.common.messages.MessageAsync;
import cl.inria.tscrypto.common.messages.PingQuery;

// TODO missing
public class PingerTask extends TimerTask {

	public PingerTask() {
	}

	public void run() {

		long ti = System.currentTimeMillis();
		System.out.println("----" + ti);
		
		Ticket ticket = Ticket.getNextTicket();
		MessageAsync pingData = new PingQuery(ticket, "ping", "replyToMe");

		// TODO send pong
		//		sd.send(pingData);

	}

}
