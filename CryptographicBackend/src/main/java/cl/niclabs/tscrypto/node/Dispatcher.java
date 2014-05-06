package cl.niclabs.tscrypto.node;

import cl.niclabs.tscrypto.common.messages.TSMessage;

import java.io.IOException;

/**
 * Created by franchoco on 17-04-14.
 */
public interface Dispatcher {
    void send(TSMessage message) throws IOException;
}
