package cl.inria.tscrypto.sigDealer.handlers;

import cl.inria.tscrypto.common.messages.TSMessage;
import cl.inria.tscrypto.sigDealer.RequestManager;

public interface SDHandler {

	void init(TSMessage message);
	void handle(RequestManager manager);
	
}
