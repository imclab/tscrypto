package cl.niclabs.tscrypto.common.messages;

import cl.niclabs.tscrypto.common.datatypes.JsonFormat;

// TODO: Complete this envelope.
public class SignedEnvelope extends JsonFormat {
    private JsonFormat data;
    private String signature;

    public SignedEnvelope() {
    }

    public SignedEnvelope(JsonFormat data, String signature) {
        this.data = data;
        this.signature = signature;
    }

}
