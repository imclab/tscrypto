package cl.inria.tscrypto.common.messages;

import cl.inria.tscrypto.common.datatypes.Ticket;

public class DeleteKeyAnswer extends MessageAsync {
    private int nodeId;

    public DeleteKeyAnswer() {
    }

    public DeleteKeyAnswer(Ticket ticket, int nodeId, String replyTo) {
        super("deleteKey-answer", "1.0", ticket, replyTo);
        this.nodeId = nodeId;
    }

    public int getNodeId() {
        return nodeId;
    }
}
