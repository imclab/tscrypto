package cl.inria.tscrypto.common.messages;

import cl.inria.tscrypto.common.datatypes.Ticket;

public class DeleteKeyQuery extends MessageAsync {
    private String label;
    public DeleteKeyQuery() {}

    public DeleteKeyQuery(String label, Ticket ticket, String replyTo) {
        super("deleteKey-query", "1.0", ticket, replyTo);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
