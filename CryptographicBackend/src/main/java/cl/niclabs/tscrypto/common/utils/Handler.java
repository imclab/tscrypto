package cl.niclabs.tscrypto.common.utils;

import cl.niclabs.tscrypto.common.messages.TSMessage;

public interface Handler {
    void init(TSMessage message);
}
