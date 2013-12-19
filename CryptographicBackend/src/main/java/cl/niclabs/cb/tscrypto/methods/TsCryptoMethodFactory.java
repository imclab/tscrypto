package cl.niclabs.cb.tscrypto.methods;

import cl.inria.tscrypto.keyFactory.KeyDispatchRequestManager;
import cl.inria.tscrypto.sigDealer.*;
import cl.niclabs.cb.backend.Method;
import cl.niclabs.cb.backend.SessionManager;
import cl.niclabs.cb.backend.methods.*;
import cl.niclabs.cb.jcrypto.SessionManagerImpl;
import cl.niclabs.cb.jcrypto.methods.DigestInitMethodImpl;
import cl.niclabs.cb.jcrypto.methods.DigestMethodImpl;
import cl.niclabs.cb.jcrypto.methods.GenerateRandomMethodImpl;
import cl.niclabs.cb.jcrypto.methods.SeedRandomMethodImpl;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;

public class TsCryptoMethodFactory implements MethodFactory, Closeable {
    // Esta versión ocupa algunas clases de jcrypto...
    private H2KeyManager keyManager;
    private int k;
    private int l;

    private KeyDispatchRequestManager keyRequestManager;
    private RequestManager requestManager;
    private SessionManager sessionManager;

    public TsCryptoMethodFactory(SDConfig config, RequestManager requestManager, KeyDispatchRequestManager keyRequestManager) throws IOException {
        //keyManager = new HashTableKeyManager();
        try {
            keyManager = new H2KeyManager();
        } catch (ClassNotFoundException | SQLException e ) {
            throw new IOException(e);
        }

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

    @Override
    public void close() throws IOException {
        keyManager.close();
    }
}
