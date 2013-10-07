package cl.niclabs.cb.tscrypto.methods;

import cl.inria.tscrypto.keyFactory.KeyDispatcher;
import cl.inria.tscrypto.keyFactory.KeyManagementCollector;
import cl.inria.tscrypto.sigDealer.Dispatcher;
import cl.inria.tscrypto.node.KeyManager;
import cl.inria.tscrypto.sigDealer.RequestManager;
import cl.inria.tscrypto.sigDealer.ResultsCollector;
import cl.inria.tscrypto.sigDealer.SDConfig;

import cl.niclabs.cb.backend.methods.*;
import com.rabbitmq.client.Connection;

import java.io.IOException;

public class TsCryptoMethodFactory implements MethodFactory {
    private final KeyManager keyManager;

    private final RequestManager requestManager;

    private ResultsCollector resultsCollector;
    private Dispatcher dispatcher;

    private KeyManagementCollector keyManagementCollector;
    private KeyDispatcher keyDispatcher;

    private Signer signer;

    public TsCryptoMethodFactory(Connection connection, SDConfig config) throws IOException {

        keyManager = new KeyManager();
        requestManager = RequestManager.getInstance();
        resultsCollector = new ResultsCollector(config, connection, requestManager);
        dispatcher = new Dispatcher(config.getRabbitMQConfig(), connection);
        keyManagementCollector = new KeyManagementCollector(config, connection);
        keyDispatcher = new KeyDispatcher(connection, config.getRabbitMQConfig());
        signer = new Signer(keyManager, requestManager);

        requestManager.init(
                dispatcher,
                config.getRabbitMQConfig().getClientQueue(),
                config.getRabbitMQConfig().getSignRequestAlias()
        );
    }

    @Override
    public DeleteKeyPairMethod makeDeleteKeyMethod(DeleteKeyPairMethod.Args args) {
        return new DeleteKeyPairMethodImpl(args, keyManager);
    }

    @Override
    public FindKeyMethod makeFindKeyMethod(FindKeyMethod.Args args) {
        return new FindKeyMethodImpl(args, keyManager);
    }

    @Override
    public GenerateKeyPairMethod makeGenerateKeyPairMethod(GenerateKeyPairMethod.Args args) {
        return new GenerateKeyPairMethodImpl(args, keyManager, keyDispatcher);
    }

    @Override
    public GetAttributeMethod makeGetAttributeMethod(GetAttributeMethod.Args args) {
        // TODO!
        return null;
    }

    @Override
    public SignInitMethod makeSignInitMethod(SignInitMethod.Args args) {
        return new SignInitMethodImpl(args, signer);
    }

    @Override
    public SignMethod makeSignMethod(SignMethod.Args args) {
        return new SignMethodImpl(args, signer);
    }

    public ResultsCollector getResultsCollector() {
        return resultsCollector;
    }

    public KeyManagementCollector getKeyManagementCollector() {
        return keyManagementCollector;
    }
}
