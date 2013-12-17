package cl.inria.tscrypto.node.handlers;

import cl.inria.tscrypto.common.messages.MessageAsync;
import cl.inria.tscrypto.common.messages.TSMessage;
import cl.inria.tscrypto.node.Signer;

public interface NodeHandler {

	public void init(TSMessage message);
	public MessageAsync handle(Signer signer);

}
