package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.Method;
import cl.niclabs.cb.backend.methods.*;
import cl.niclabs.cb.jcrypto.SessionManagerImpl;

public class SimpleSignMethodFactory implements MethodFactory {

    private static SimpleSignMethodFactory instance = null;

    public static SimpleSignMethodFactory getInstance() {
        if (instance == null) {
            synchronized (SimpleSignMethodFactory.class) {
                if (instance == null) {
                    instance = new SimpleSignMethodFactory();
                }
            }
        }
        return instance;
    }

    private SimpleSignMethodFactory() {

    }

    @Override
    public Method makeOpenSessionMethod() {
        return new OpenSessionMethodImpl();
    }

    @Override
    public Method makeCloseSessionMethod(CloseSessionMethod.Args args) {
        return new CloseSessionMethodImpl(args);
    }

    @Override
    public Method makeDeleteKeyMethod(DeleteKeyPairMethod.Args args) {
        return new DeleteKeyPairMethodImpl(args);
    }

    @Override
    public Method makeFindKeyMethod(FindKeyMethod.Args args) {
        return new FindKeyMethodImpl(args);
    }

    @Override
    public Method makeGenerateKeyPairMethod(GenerateKeyPairMethod.Args args) {
        return new GenerateKeyPairMethodImpl(args);
    }

    @Override
    public Method makeSignInitMethod(SignInitMethod.Args args) {
        return new SignInitMethodImpl(args);
    }

    @Override
    public Method makeSignMethod(SignMethod.Args args) {
        return new SignMethodImpl(args);
    }

    @Override
    public Method makeSeedRandomMethod(SeedRandomMethod.Args args) {
        return new SeedRandomMethodImpl(args, SessionManagerImpl.getInstance());
    }

    @Override
    public Method makeGenerateRandomMethod(GenerateRandomMethod.Args args) {
        return new GenerateRandomMethodImpl(args, SessionManagerImpl.getInstance());
    }

    @Override
    public Method makeDigestInitMethod(DigestInitMethod.Args args) {
        return new DigestInitMethodImpl(args, SessionManagerImpl.getInstance());
    }

    @Override
    public Method makeDigestMethod(DigestMethod.Args args) {
        return new DigestMethodImpl(args, SessionManagerImpl.getInstance());
    }
}
