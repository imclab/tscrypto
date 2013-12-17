package cl.inria.tscrypto.common.messages;

import java.math.BigInteger;

import cl.inria.tscrypto.common.datatypes.Ticket;

/**
 * Message used to sign some data
 * @author acansado
 *
 */
public class SignShareQuery extends MessageAsync {

	private BigInteger hashedDocument;
	private String alias;
	
	public SignShareQuery(){
	}

    public SignShareQuery(Ticket ticket, BigInteger hashedDocument, String alias, String replyTo) {
		super("signShare-query", "1.0", ticket, replyTo);

		this.hashedDocument = hashedDocument;
		this.alias = alias;
	}

	
	public String getAlias(){
		return alias;
	}

	public BigInteger getHashedDocument() {
		return this.hashedDocument;
	}


}
