package cl.inria.tscrypto.sigDealer;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import cl.inria.tscrypto.common.algorithms.SignatureDealer;
import cl.inria.tscrypto.common.algorithms.SignatureDealerImpl;
import cl.inria.tscrypto.common.algorithms.SignatureRequest;
import cl.inria.tscrypto.common.datatypes.KeyInfo;
import cl.inria.tscrypto.common.datatypes.Ticket;
import cl.inria.tscrypto.common.exceptions.InvalidKey;
import cl.inria.tscrypto.common.messages.SignShareQuery;
import cl.inria.tscrypto.keyFactory.KeyDispatcher;

public class RequestManager {

	public static final long SIGNING_TIMEOUT = 40 * 1000;

	private HashMap<String, SignatureDealerImpl> signingRequests = new LinkedHashMap<>();

	// TODO unused - should be used by the garbage collector
	private HashMap<Ticket, Timer> timerRequests = new LinkedHashMap<>();

	// communication
	private Dispatcher dispatcher;
	private String replyTo;

	// key=alias
	private Map<String, KeyInfo> keys = new Hashtable<>();
    private String signRequestAlias;

    public RequestManager(Dispatcher dispatcher, String replyTo, String signRequestAlias) {
        this.dispatcher = dispatcher;
        this.replyTo = replyTo;
        this.signRequestAlias = signRequestAlias;
    }

	public void addKey(KeyInfo keyInfo) {
        synchronized (keys) {
		    this.keys.put(keyInfo.getKeyMetaInfo().getAlias(), keyInfo);
        }
	}

    private void submitRequest(String hashAlgorithm, byte[] blob, String alias, Ticket ticket)
            throws IOException, InvalidKey, NoSuchAlgorithmException {
        KeyInfo key = keys.get(alias);

        if (key == null) {
            throw new InvalidKey(alias);
        }

        SignatureDealerImpl signatureDealer = new SignatureDealerImpl(key);
        SignatureRequest request = signatureDealer.prepareSignature(blob, hashAlgorithm);

        synchronized (signingRequests){
            signingRequests.put(ticket.getId(), signatureDealer);
        }

        Timer timerRequest = new Timer();

        synchronized (timerRequests) {
            timerRequests.put(ticket, timerRequest);
        }

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

	/**
	 * Signs data
	 * 
	 * @param dataToBeSigned
	 * @param alias
	 *            public key alias used for submitting the request
	 * @return
	 * @throws IOException
	 *             problem connecting to RabbitMQ
	 * @throws InvalidKey
	 *             could not public key 'alias'
	 */
	public Ticket sign(byte[] dataToBeSigned, String alias) throws IOException,
            InvalidKey {

		Ticket ticket = Ticket.getNextTicket();
        try {
		    submitRequest("Sha1", dataToBeSigned, alias, ticket);
        } catch (NoSuchAlgorithmException e) {

        }

        return ticket;
	}

    /**
     *
     * @param hashAlgorithm it can be Sha1 or NONE
     * @param dataToBeSigned
     * @param alias public key alias user for submitting the request
     * @return
     * @throws IOException when there's a problem connecting to RabbitMQ
     * @throws InvalidKey
     * @throws NoSuchAlgorithmException when it can't use the requested hashAlgorithm
     */
    public Ticket sign(String hashAlgorithm, byte[] dataToBeSigned, String alias) throws IOException,
            InvalidKey, NoSuchAlgorithmException {

        Ticket ticket = Ticket.getNextTicket();
        submitRequest(hashAlgorithm, dataToBeSigned, alias, ticket);
        return ticket;
    }

    public synchronized void removeKey(String privateKeyHandler) {
        synchronized (keys) {
            keys.remove(privateKeyHandler);
        }
    }
}
