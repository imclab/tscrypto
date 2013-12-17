package cl.inria.tscrypto.common.messages;

import cl.inria.tscrypto.common.datatypes.Ticket;


public class SendKeyAnswer extends MessageAsync {
    private int nodeId;

    public SendKeyAnswer() {
    }

    public SendKeyAnswer(Ticket ticket, int nodeId, String replyTo) {
        super("sendKey-answer", "1.0", ticket, replyTo);
        this.nodeId = nodeId;
    }

    public int getNodeId() {
        return nodeId;
    }
}
