package cl.inria.tscrypto.node.handlers;

import cl.inria.tscrypto.common.messages.MessageAsync;
import cl.inria.tscrypto.common.messages.PingQuery;
import cl.inria.tscrypto.common.messages.PingAnswer;
import cl.inria.tscrypto.common.messages.TSMessage;
import cl.inria.tscrypto.node.Signer;

public class PingHandler implements NodeHandler {

	private PingQuery message;
	
	@Override
	public void init(TSMessage message) {
		this.message = (PingQuery) message;
	}

	@Override
	public MessageAsync handle(Signer signer) {

		PingAnswer response =
			new PingAnswer(
					message.ticket,
					signer.getId(),
					message.getPingType(),
					message.getReplyTo()
					);

		return response;
	}

}
