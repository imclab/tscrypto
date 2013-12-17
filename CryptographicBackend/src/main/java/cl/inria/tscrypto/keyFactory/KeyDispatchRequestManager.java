package cl.inria.tscrypto.keyFactory;

import cl.inria.tscrypto.common.datatypes.KeyInfo;
import cl.inria.tscrypto.common.datatypes.Ticket;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class KeyDispatchRequestManager {
    private Map<String, KeyDispatchRequest> keyDispatchingRequest = new Hashtable<>();
    private KeyDispatcher keyDispatcher;

    public KeyDispatchRequestManager(KeyDispatcher keyDispatcher) {
        this.keyDispatcher = keyDispatcher;
    }

    public KeyDispatchRequest getKeyDispatchRequest(Ticket ticket) {
        return keyDispatchingRequest.get(ticket.getId());
    }

    public synchronized void removeRequest(Ticket ticket) {
        keyDispatchingRequest.remove(ticket);
    }

    public Ticket dispatch(KeyInfo keyInfo) throws IOException {
        Ticket ticket = Ticket.getNextTicket();
        KeyDispatchRequest request = new KeyDispatchRequest(keyInfo);
        keyDispatchingRequest.put(ticket.getId(), request);
        keyDispatcher.dispatch(keyInfo, ticket);

        return ticket;
    }

}
