package cl.inria.tscrypto.node.handlers;

import cl.inria.tscrypto.common.datatypes.SignatureShare;
import cl.inria.tscrypto.common.messages.MessageAsync;
import cl.inria.tscrypto.common.messages.SignShareQuery;
import cl.inria.tscrypto.common.messages.SignShareAnswer;
import cl.inria.tscrypto.common.messages.TSMessage;
import cl.inria.tscrypto.common.utils.TSLogger;
import cl.inria.tscrypto.node.Signer;

public class SignHandler implements NodeHandler {

	private SignShareQuery message;
	
	@Override
	public void init(TSMessage message) {
		this.message = (SignShareQuery) message;
	}
	
	@Override
	public MessageAsync handle(Signer signer) {
		SignatureShare signatureShare = signer.sign(message.getHashedDocument(),message.getAlias());

		TSLogger.node.info("Document signed");

		SignShareAnswer response = new SignShareAnswer(
				message.ticket,
				signatureShare,
				signer.getId(),
				message.getReplyTo());
		
		return response;
	}
}
