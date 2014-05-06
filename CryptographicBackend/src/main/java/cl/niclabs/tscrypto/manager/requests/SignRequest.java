package cl.niclabs.tscrypto.manager.requests;

import cl.niclabs.tscrypto.common.algorithms.dealer.SignatureDealer;
import cl.niclabs.tscrypto.common.algorithms.dealer.SignatureRequest;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.manager.Request;

import java.security.NoSuchAlgorithmException;

// Signature Request Wrapper to user the Request interface.
public class SignRequest extends Request {
    private final SignatureDealer dealer;

    public SignRequest(SignatureDealer dealer) throws NoSuchAlgorithmException {
        this.dealer = dealer;
    }

    @Override
    public void setReady(int nodeId) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected boolean isReady() {
        return getDealer().isDone();
    }

    @Override
    public void waitUntilReady() throws InterruptedException {
        SignatureRequest request = dealer.getRequest();
        while(!isReady()) {
            synchronized (request) {
                while(!isReady()) {
                    TSLogger.sd.debug("Waiting node signature shares.");
                    request.wait();
                }
            }
        }
    }

    public SignatureDealer getDealer() {
        return dealer;
    }

}
