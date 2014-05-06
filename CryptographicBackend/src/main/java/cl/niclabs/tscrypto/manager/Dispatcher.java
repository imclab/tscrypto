package cl.niclabs.tscrypto.manager;

import cl.niclabs.tscrypto.common.datatypes.JsonFormat;
import cl.niclabs.tscrypto.common.messages.TSMessage;

import java.io.IOException;

public interface Dispatcher {
    void dispatch(String alias, JsonFormat message) throws IOException;
    void dispatchEncrypted(String envelope, JsonFormat message) throws IOException;
}
