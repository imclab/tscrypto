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

package cl.niclabs.tscrypto.manager;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import cl.niclabs.tscrypto.common.datatypes.*;
import cl.niclabs.tscrypto.common.algorithms.dealer.SignatureDealer;
import cl.niclabs.tscrypto.common.algorithms.dealer.SignatureRequest;
import cl.niclabs.tscrypto.common.exceptions.InvalidKey;
import cl.niclabs.tscrypto.common.messages.*;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.manager.requests.DeleteKeyRequest;
import cl.niclabs.tscrypto.manager.requests.KeyDispatchRequest;
import cl.niclabs.tscrypto.manager.requests.SignRequest;
import zmq.Req;


/**
 * The objects of this instances manages the timelife of
 * the requests that had been done to the nodes
 */
public class RequestManager {

    // Refactor this. // Make a proxy that implements the Request interface
	// private final HashMap<String, SignatureDealer> signingRequests = new LinkedHashMap<>();
    private final Map<Ticket, Request> requests = new Hashtable<>();

	// communication
	private Dispatcher dispatcher;

	private String replyTo; // TODO: clean this...

	// key=alias
    private String envelope;
    private String keyEnvelope;

    public RequestManager(Dispatcher dispatcher, String replyTo, String envelope, String keyEnvelope) {
        this.dispatcher = dispatcher;
        this.replyTo = replyTo;
        this.envelope = envelope;
        this.keyEnvelope = keyEnvelope;
    }

    public Ticket sign(KeyMetaInfo keyMetaInfo, TSPublicKey publicKey, String hashAlgorithm, byte[] dataToBeSigned, String alias)
            throws IOException, InvalidKey, NoSuchAlgorithmException {

        Ticket ticket = Ticket.getNextTicket();

        SignatureDealer signatureDealer = SignatureDealerFactory.getInstance(keyMetaInfo, publicKey);
        BigInteger hashedDocument = signatureDealer.prepareSignature(dataToBeSigned, hashAlgorithm).getHashedDocument();

        synchronized (requests) {
            requests.put(ticket, new SignRequest(signatureDealer));
        }

        SignShareQuery message = new SignShareQuery(
                ticket,
                hashedDocument,
                alias,
                replyTo
        );

        dispatcher.dispatch(envelope, message);

        return ticket;
    }

    public Ticket dispatchKey(KeyInfo keyInfo) throws IOException {
        Ticket ticket = Ticket.getNextTicket();

        requests.put(ticket, new KeyDispatchRequest(keyInfo));

        String label = keyInfo.getKeyMetaInfo().getAlias();

        for(int i=0; i<keyInfo.getKeyMetaInfo().getL(); i++) {
            KeyShareInfo keyShareInfo = new KeyShareInfo(
                    keyInfo.getKeyMetaInfo(),
                    keyInfo.getPublicKey(),
                    keyInfo.getKeyShares().getSecret(i)
            );

            SendKeyQuery query = new SendKeyQuery(
                    ticket,
                    label,
                    keyShareInfo,
                    keyEnvelope
            );


            EncryptedData encryptedData = new EncryptedData("node" + i, query.toJson().getBytes());
            dispatcher.dispatch(keyEnvelope + i, encryptedData);
        }

        return ticket;
    }

    public Ticket deleteKey(String label) throws IOException {
        TSLogger.sd.debug("Deleting key with label: " + label);
        Ticket ticket = Ticket.getNextTicket();
        Request request = new DeleteKeyRequest();
        requests.put(ticket, request);

        DeleteKeyQuery query = new DeleteKeyQuery(label, ticket, replyTo);
        dispatcher.dispatch(envelope, query);

        return ticket;
    }

    // Automatic upcast (?)
    public <T extends Request> T getRequest(Ticket ticket) {
        synchronized (requests) {
            Request request = requests.get(ticket);
            return (T) request;
        }
    }

    public synchronized void removeRequest(Ticket ticket) {
        requests.remove(ticket);
    }
}

