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

package cl.niclabs.tscrypto.sigDealer.handlers;

import java.security.NoSuchAlgorithmException;

import cl.niclabs.tscrypto.common.algorithms.SignatureDealer;
import cl.niclabs.tscrypto.common.messages.SignShareAnswer;
import cl.niclabs.tscrypto.common.messages.TSMessage;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.sigDealer.RequestManager;

public class SignedShareHandler implements SDHandler {

	private SignShareAnswer message;
	
	@Override
	public void init(TSMessage message) {
		this.message = (SignShareAnswer) message;
	}

	
	@Override
	public void handle(RequestManager manager) {
		SignatureDealer request = manager.getSigningRequest(message.ticket);
		if (request == null) {
			TSLogger.sd.info("Could not find signing request " + message.ticket);
			return;
		}
		
		if (request.isDone()) {
			TSLogger.sd.info("Signature Share will not used: | Node: " + message.node_id + "| Ticket: "
					+ message.ticket.getId());
			return;
		}

		TSLogger.sd.info("Signature Share will be used: | Node: " + message.node_id + "| Ticket: "
				+ message.ticket.getId());

		try {
			request.joinSignatureShare(message.signature, message.node_id);
		} catch (NoSuchAlgorithmException e) {
			TSLogger.sd.error("Could not find hash algorithm:" + e.getMessage());
		}

	}
}
