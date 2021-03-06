/*
    Copyright 2013 NIC Chile Research Labs
    This file is part of TsCrypto.

    TsCrypto is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TsCrypto is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TsCrypto.  If not, see <http://www.gnu.org/licenses/>.
 */

package cl.niclabs.tscrypto.manager.handlers;

import java.security.NoSuchAlgorithmException;

import cl.niclabs.tscrypto.common.algorithms.dealer.SignatureDealer;
import cl.niclabs.tscrypto.common.messages.SignShareAnswer;
import cl.niclabs.tscrypto.common.messages.TSMessage;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.manager.DealerHandler;
import cl.niclabs.tscrypto.manager.RequestManager;
import cl.niclabs.tscrypto.manager.requests.SignRequest;

public class SignedShareHandler implements DealerHandler {

	private SignShareAnswer message;
	
	@Override
	public void init(TSMessage message) {
		this.message = (SignShareAnswer) message;
	}

	
	@Override
	public void handle(RequestManager manager) {
        SignRequest request = manager.getRequest(message.ticket);
        SignatureDealer dealer = request.getDealer();
		if (dealer == null) {
			TSLogger.sd.info("Could not find signing request " + message.ticket);
			return;
		}
		
		if (dealer.isDone()) {
			TSLogger.sd.info("Signature Share will not used: | Node: " + message.node_id + "| Ticket: "
					+ message.ticket.getId());
			return;
		}

		TSLogger.sd.info("Signature Share will be used: | Node: " + message.node_id + "| Ticket: "
				+ message.ticket.getId());

		try {
            dealer.joinSignatureShare(message.signature, message.node_id);
		} catch (NoSuchAlgorithmException e) {
			TSLogger.sd.error("Could not find hash algorithm:" + e.getMessage());
		}
	}
}
