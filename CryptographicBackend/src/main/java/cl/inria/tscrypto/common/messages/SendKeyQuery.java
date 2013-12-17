package cl.inria.tscrypto.common.messages;

import cl.inria.tscrypto.common.datatypes.KeyShareInfo;
import cl.inria.tscrypto.common.datatypes.Ticket;

public class SendKeyQuery extends MessageAsync {
    private String label;
    private KeyShareInfo keyShareInfo;

    public SendKeyQuery() {

    }

    public SendKeyQuery(Ticket ticket, String label, KeyShareInfo keyShareInfo, String replyTo) {
        super("sendKey-query", "1.0", ticket, replyTo);
        this.label = label;
        this.keyShareInfo = keyShareInfo;
    }

    public String getLabel() {
        return label;
    }

    public KeyShareInfo getKeyInfo() {
        return keyShareInfo;
    }
}
