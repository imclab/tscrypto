package cl.niclabs.cb.tscrypto.methods;

import cl.inria.tscrypto.common.datatypes.KeyInfo;
import cl.inria.tscrypto.keyFactory.KeyDispatcher;
import cl.inria.tscrypto.keyFactory.algorithm.KeyFactory;
import cl.inria.tscrypto.node.KeyManager;
import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.GenerateKeyPairMethod;

import java.io.IOException;

public class GenerateKeyPairMethodImpl extends GenerateKeyPairMethod {
    private final KeyManager keyManager;
    private final KeyDispatcher keyDispatcher;

    public GenerateKeyPairMethodImpl(Args args, KeyManager keyManager, KeyDispatcher keyDispatcher) {
        super(args);
        this.keyManager = keyManager;
        this.keyDispatcher = keyDispatcher;
    }

    @Override
    public ResponseMessage execute() {
        try {
            // TODO: poner k y l en configuración...
            KeyInfo keyInfo = KeyFactory.generateKeys(this.keySize, 2, 4);
            String handler = keyInfo.getKeyMetaInfo().getAlias();
            keyDispatcher.dispatch(keyInfo);
            keyManager.addKey(keyInfo);
            return ResponseMessage.OKMessage(new ReturnValue(handler));
        } catch (IOException e) {
            return ResponseMessage.ErrorMessage(e.getMessage());
        }
    }
}
