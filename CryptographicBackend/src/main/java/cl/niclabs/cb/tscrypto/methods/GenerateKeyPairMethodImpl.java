package cl.niclabs.cb.tscrypto.methods;

import cl.inria.tscrypto.common.datatypes.KeyInfo;
import cl.inria.tscrypto.common.datatypes.Ticket;
import cl.inria.tscrypto.common.utils.TSLogger;
import cl.inria.tscrypto.keyFactory.KeyDispatchRequest;
import cl.inria.tscrypto.keyFactory.KeyDispatchRequestManager;
import cl.inria.tscrypto.keyFactory.algorithm.KeyFactory;
import cl.inria.tscrypto.sigDealer.KeyManager;
import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.GenerateKeyPairMethod;

import java.io.IOException;

class GenerateKeyPairMethodImpl implements GenerateKeyPairMethod {
    protected final String keyType;
    protected final int keySize;
    protected final String publicExponent;
    private final KeyManager keyManager;
    private final KeyDispatchRequestManager keyRequestDispatcher;
    private final int k;
    private final int l;

    public GenerateKeyPairMethodImpl(Args args, KeyManager keyManager, KeyDispatchRequestManager keyRequestManager, int k, int l) {
        keyType = args.keyType;
        keySize = args.keySize;
        publicExponent = args.publicExponent;
        this.keyManager = keyManager;
        this.keyRequestDispatcher = keyRequestManager;
        this.k = k;
        this.l = l;
    }

    @Override
    public ResponseMessage execute() {
        try {
            TSLogger.keyFactory.debug("KeyPair Generation with k="+k+" and l="+l);
            KeyInfo keyInfo = KeyFactory.generateKeys(this.keySize, k, l);
            TSLogger.keyFactory.debug("KeyPair Generation successful");
            String handler = keyInfo.getKeyMetaInfo().getAlias();
            Ticket ticket = keyRequestDispatcher.dispatch(keyInfo);
            KeyDispatchRequest request = keyRequestDispatcher.getKeyDispatchRequest(ticket);

            request.waitUntilReady();

            keyRequestDispatcher.removeRequest(ticket);
            keyManager.addKey(keyInfo);

            return ResponseMessage.OKMessage(new ReturnValue(handler));
        } catch (IOException e) {
            return ResponseMessage.ErrorMessage(e.getMessage());
        } catch (InterruptedException e) {
            return ResponseMessage.ErrorMessage(e.getMessage());
        }
    }
}
