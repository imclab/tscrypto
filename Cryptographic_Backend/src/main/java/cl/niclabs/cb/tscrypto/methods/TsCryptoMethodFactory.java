package cl.niclabs.cb.tscrypto.methods;

import cl.inria.tscrypto.keyFactory.KeyDispatcher;
import cl.inria.tscrypto.keyFactory.KeyManagementCollector;
import cl.inria.tscrypto.node.KeyManager;
import cl.inria.tscrypto.sigDealer.Dispatcher;
import cl.inria.tscrypto.sigDealer.RequestManager;
import cl.inria.tscrypto.sigDealer.ResultsCollector;
import cl.inria.tscrypto.sigDealer.SDConfig;
import cl.niclabs.cb.backend.Method;
import cl.niclabs.cb.backend.methods.*;

import cl.niclabs.cb.jcrypto.methods.DigestInitMethodImpl;
import cl.niclabs.cb.jcrypto.methods.DigestMethodImpl;
import cl.niclabs.cb.jcrypto.methods.GenerateRandomMethodImpl;
import cl.niclabs.cb.jcrypto.methods.SeedRandomMethodImpl;
import com.rabbitmq.client.Connection;

import java.io.IOException;

@SuppressWarnings("UnusedDeclaration")
public class TsCryptoMethodFactory implements MethodFactory {
    /*
        Esta versión ocupa algunas clases de jcrypto...
     */

    private final KeyManager keyManager;

    private final RequestManager requestManager;

    private ResultsCollector resultsCollector;
    private Dispatcher dispatcher;

    private KeyManagementCollector keyManagementCollector;
    private KeyDispatcher keyDispatcher;

    public TsCryptoMethodFactory(Connection connection, SDConfig config) throws IOException {

        keyManager = new KeyManager();
        requestManager = RequestManager.getInstance();
        resultsCollector = new ResultsCollector(config, connection, requestManager);
        dispatcher = new Dispatcher(config.getRabbitMQConfig(), connection);
        keyManagementCollector = new KeyManagementCollector(config, connection);
        keyDispatcher = new KeyDispatcher(connection, config.getRabbitMQConfig());

        requestManager.init(
                dispatcher,
                config.getRabbitMQConfig().getClientQueue(),
                config.getRabbitMQConfig().getSignRequestAlias()
        );
    }

    @Override
    public Method makeOpenSessionMethod() {
        // Uses SessionManager from jcrypto
        return new OpenSessionMethodImpl(keyManager, requestManager);
    }

    @Override
    public Method makeCloseSessionMethod(CloseSessionMethod.Args args) {
        // Uses SessionManager from jcrypto
        return new CloseSessionMethodImpl(args);
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
        return new GenerateKeyPairMethodImpl(args, keyManager, keyDispatcher);
    }

    @Override
    public Method makeSignInitMethod(SignInitMethod.Args args) {
        return new SignInitMethodImpl(args);
    }

    @Override
    public Method makeSignMethod(SignMethod.Args args) {
        return new SignMethodImpl(args);
    }

    public ResultsCollector getResultsCollector() {
        return resultsCollector;
    }

    public KeyManagementCollector getKeyManagementCollector() {
        return keyManagementCollector;
    }

    @Override
    public Method makeSeedRandomMethod(SeedRandomMethod.Args args) {
        return new SeedRandomMethodImpl(args);
    }

    @Override
    public Method makeGenerateRandomMethod(GenerateRandomMethod.Args args) {
        return new GenerateRandomMethodImpl(args);
    }

    @Override
    public Method makeDigestInitMethod(DigestInitMethod.Args args) {
        return new DigestInitMethodImpl(args);
    }

    @Override
    public Method makeDigestMethod(DigestMethod.Args args) {
        return new DigestMethodImpl(args);
    }
}
