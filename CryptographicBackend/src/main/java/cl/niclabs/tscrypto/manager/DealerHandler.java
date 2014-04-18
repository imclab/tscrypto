package cl.niclabs.tscrypto.manager;

import cl.niclabs.tscrypto.common.utils.Handler;

public interface DealerHandler extends Handler {
    void handle(RequestManager context);
}
