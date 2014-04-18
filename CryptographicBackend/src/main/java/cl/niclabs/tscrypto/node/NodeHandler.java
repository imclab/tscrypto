package cl.niclabs.tscrypto.node;

import cl.niclabs.tscrypto.common.messages.MessageAsync;
import cl.niclabs.tscrypto.common.utils.Handler;
import cl.niclabs.tscrypto.node.keyManagement.KeyShareManager;

public interface NodeHandler extends Handler {
    MessageAsync handle(KeyShareManager context);
}
