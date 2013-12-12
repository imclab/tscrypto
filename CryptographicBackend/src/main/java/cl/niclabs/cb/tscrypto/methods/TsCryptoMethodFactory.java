package cl.niclabs.cb.tscrypto.methods;

import cl.inria.tscrypto.keyFactory.KeyDispatchRequestManager;
import cl.inria.tscrypto.keyFactory.KeyDispatcher;
import cl.inria.tscrypto.keyFactory.KeyManagementCollector;
import cl.inria.tscrypto.sigDealer.KeyManager;
import cl.inria.tscrypto.sigDealer.Dispatcher;
import cl.inria.tscrypto.sigDealer.RequestManager;
import cl.inria.tscrypto.sigDealer.ResultsCollector;
import cl.inria.tscrypto.sigDealer.SDConfig;
import cl.niclabs.cb.backend.Method;
import cl.niclabs.cb.backend.SessionManager;
import cl.niclabs.cb.backend.methods.*;
import cl.niclabs.cb.jcrypto.SessionManagerImpl;
import cl.niclabs.cb.jcrypto.methods.DigestInitMethodImpl;
import cl.niclabs.cb.jcrypto.methods.DigestMethodImpl;
import cl.niclabs.cb.jcrypto.methods.GenerateRandomMethodImpl;
import cl.niclabs.cb.jcrypto.methods.SeedRandomMethodImpl;
import com.rabbitmq.client.Connection;

import java.io.Closeable;
import java.io.IOException;

public class TsCryptoMethodFactory implements MethodFactory {
    // Esta versión ocupa algunas clases de jcrypto...
    private KeyManager keyManager;
    private int k;
    private int l;

    private KeyDispatchRequestManager keyRequestManager;
    private RequestManager requestManager;
    private SessionManager sessionManager;

    public TsCryptoMethodFactory(SDConfig config, RequestManager requestManager, KeyDispatchRequestManager keyRequestManager) throws IOException {
        keyManager = new KeyManager();
        sessionManager = new SessionManagerImpl();

        this.keyRequestManager = keyRequestManager;
        this.requestManager = requestManager;

        k = config.getK();
        l = config.getL();
    }

    @Override
    public Method makeOpenSessionMethod() {
        // Uses SessionManager from jcrypto
        return new OpenSessionMethodImpl(keyManager, requestManager, sessionManager);
    }

    @Override
    public Method makeCloseSessionMethod(CloseSessionMethod.Args args) {
        // Uses SessionManager from jcrypto
        return new CloseSessionMethodImpl(args, sessionManager);
    }

    @Override
    public Method makeDeleteKeyMethod(DeleteKeyPairMethod.Args args) {
        return new DeleteKeyPairMethodImpl(args, keyManager);
    }

    @Override
    public Method makeFindKeyMethod(FindKeyMethod.Args args) {
        return new FindKeyMethodImpl(args, keyManager);
    }

    @Override
    public Method makeGenerateKeyPairMethod(GenerateKeyPairMethod.Args args) {
        return new GenerateKeyPairMethodImpl(args, keyManager, keyRequestManager, k, l);
    }

    @Override
    public Method makeSignInitMethod(SignInitMethod.Args args) {
        return new SignInitMethodImpl(args, sessionManager);
    }

    @Override
    public Method makeSignMethod(SignMethod.Args args) {
        return new SignMethodImpl(args, sessionManager);
    }

    @Override
    public Method makeSeedRandomMethod(SeedRandomMethod.Args args) {
        return new SeedRandomMethodImpl(args, sessionManager);
    }

    @Override
    public Method makeGenerateRandomMethod(GenerateRandomMethod.Args args) {
        return new GenerateRandomMethodImpl(args, sessionManager);
    }

    @Override
    public Method makeDigestInitMethod(DigestInitMethod.Args args) {
        return new DigestInitMethodImpl(args, sessionManager);
    }

    @Override
    public Method makeDigestMethod(DigestMethod.Args args) {
        return new DigestMethodImpl(args, sessionManager);
    }

}
