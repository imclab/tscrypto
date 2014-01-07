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

package cl.niclabs.tscrypto.common.messages;

import java.math.BigInteger;

import cl.niclabs.tscrypto.common.datatypes.Ticket;

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
