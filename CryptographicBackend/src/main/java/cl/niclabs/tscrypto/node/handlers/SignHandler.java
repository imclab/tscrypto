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

package cl.niclabs.tscrypto.node.handlers;

import cl.niclabs.tscrypto.common.datatypes.SignatureShare;
import cl.niclabs.tscrypto.common.messages.MessageAsync;
import cl.niclabs.tscrypto.common.messages.SignShareQuery;
import cl.niclabs.tscrypto.common.messages.SignShareAnswer;
import cl.niclabs.tscrypto.common.messages.TSMessage;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.node.NodeHandler;
import cl.niclabs.tscrypto.node.Signer;
import cl.niclabs.tscrypto.node.keyManagement.KeyShareManager;

public class SignHandler implements NodeHandler {

	private SignShareQuery message;
	
	@Override
	public void init(TSMessage message) {
		this.message = (SignShareQuery) message;
	}

    @Override
    public MessageAsync handle(KeyShareManager context) {
        Signer signer = new Signer(context);
        SignatureShare signatureShare = signer.sign(message.getHashedDocument(),message.getAlias());
        TSLogger.node.info("Document signed");

        return new SignShareAnswer(
                message.ticket,
                signatureShare,
                signer.getId(),
                message.getReplyTo()
        );
    }
}
