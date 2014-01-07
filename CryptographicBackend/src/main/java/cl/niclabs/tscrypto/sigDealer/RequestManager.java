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

package cl.niclabs.tscrypto.sigDealer;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import cl.niclabs.tscrypto.common.algorithms.SignatureDealer;
import cl.niclabs.tscrypto.common.algorithms.SignatureDealerImpl;
import cl.niclabs.tscrypto.common.algorithms.SignatureRequest;
import cl.niclabs.tscrypto.common.datatypes.KeyInfo;
import cl.niclabs.tscrypto.common.datatypes.KeyMetaInfo;
import cl.niclabs.tscrypto.common.datatypes.TSPublicKey;
import cl.niclabs.tscrypto.common.datatypes.Ticket;
import cl.niclabs.tscrypto.common.exceptions.InvalidKey;
import cl.niclabs.tscrypto.common.messages.DeleteKeyQuery;
import cl.niclabs.tscrypto.common.messages.SignShareQuery;
import cl.niclabs.tscrypto.common.utils.TSLogger;


/**
 * The objects of this instances manages the timelife of
 * the requests that had been done to the nodes
 */
public class RequestManager {
	// public static final long SIGNING_TIMEOUT = 40 * 1000;

    // Refactor this. // Make a proxy that implements the Request interface
	private final HashMap<String, SignatureDealerImpl> signingRequests = new LinkedHashMap<>();
    private final Map<String, Request> requests = new Hashtable<>();

	// TODO unused - should be used by the garbage collector
	// private final HashMap<Ticket, Timer> timerRequests = new LinkedHashMap<>();

	// communication
	private Dispatcher dispatcher;
    // TODO: remove this...
    private KeyDispatcher keyDispatcher;

	private String replyTo;

	// key=alias
    private String signRequestAlias;

    public RequestManager(Dispatcher dispatcher, KeyDispatcher keyDispatcher, String replyTo, String signRequestAlias) {
        this.dispatcher = dispatcher;
        this.keyDispatcher = keyDispatcher;
        this.replyTo = replyTo;
        this.signRequestAlias = signRequestAlias;
    }

    private void submitRequest(KeyMetaInfo keyMetaInfo, TSPublicKey publicKey, String hashAlgorithm, byte[] blob, String alias, Ticket ticket)
            throws IOException, InvalidKey, NoSuchAlgorithmException {

        SignatureDealerImpl signatureDealer = new SignatureDealerImpl(keyMetaInfo, publicKey);

        SignatureRequest request = signatureDealer.prepareSignature(blob, hashAlgorithm);

        synchronized (signingRequests){
            signingRequests.put(ticket.getId(), signatureDealer);
        }


        // Timer timerRequest = new Timer();

        // synchronized (timerRequests) {
        //     timerRequests.put(ticket, timerRequest);
        // }

        // TODO check if timer is still needed
        // TimerTask timeoutTask = new TimeoutTask(signingRequest);
        // timerRequest.schedule(timeoutTask, SIGNING_TIMEOUT);

        SignShareQuery message = new SignShareQuery(
                ticket,
                request.getHashedDocument(),
                alias,
                replyTo
        );

        dispatcher.send(signRequestAlias, message);
    }

    public Ticket sign(KeyMetaInfo keyMetaInfo, TSPublicKey publicKey, String hashAlgorithm, byte[] dataToBeSigned, String alias) throws IOException,
            InvalidKey, NoSuchAlgorithmException {

        Ticket ticket = Ticket.getNextTicket();
        submitRequest(keyMetaInfo, publicKey, hashAlgorithm, dataToBeSigned, alias, ticket);
        return ticket;
    }

    public Ticket dispatchKey(KeyInfo keyInfo) throws IOException {
        Ticket ticket = Ticket.getNextTicket();

        Request request = new KeyDispatchRequest(keyInfo);
        requests.put(ticket.getId(), request);

        keyDispatcher.dispatch(keyInfo, ticket);

        return ticket;
    }

    public Ticket deleteKey(String label) throws IOException {
        TSLogger.sd.debug("Deleting key with label: " + label);
        Ticket ticket = Ticket.getNextTicket();
        Request request = new DeleteKeyRequest();
        requests.put(ticket.getId(), request);

        DeleteKeyQuery query = new DeleteKeyQuery(label, ticket, replyTo);
        dispatcher.send(signRequestAlias, query);

        return ticket;
    }

    public SignatureDealer getSigningRequest(Ticket ticket) {
        synchronized (signingRequests) {
            return signingRequests.get(ticket.getId());
        }
    }

    public SignatureRequest getSignatureRequest(Ticket ticket) {
        synchronized (signingRequests) {
            return signingRequests.get(ticket.getId()).getRequest();
        }
    }

    public KeyDispatchRequest getKeyDispatchRequest(Ticket ticket) {
        synchronized (requests) {
            return (KeyDispatchRequest) requests.get(ticket.getId());
        }
    }

    public DeleteKeyRequest getDeleteKeyRequest(Ticket ticket) {
        synchronized (requests) {
            return (DeleteKeyRequest) requests.get(ticket.getId());
        }
    }

    public synchronized void removeRequest(Ticket ticket) {
        signingRequests.remove(ticket);
        requests.remove(ticket);
    }
}

