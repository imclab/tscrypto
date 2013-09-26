package cb.backend.methods.implementation;

import cb.backend.methods.*;

public class SimpleSignMethodFactory implements MethodsFactory {

    final private static SimpleSignMethodFactory instance = new SimpleSignMethodFactory();

    public static SimpleSignMethodFactory getInstance() {
        return instance;
    }

    private SimpleSignMethodFactory() {

    }
    @Override
    public DeleteKeyPairMethod makeDeleteKeyMethod(DeleteKeyPairMethod.Args args) {
        return new DeleteKeyPairMethodImpl(args);
    }

    @Override
    public FindKeyMethod makeFindKeyMethod(FindKeyMethod.Args args) {
        return new FindKeyMethodImpl(args);
    }

    @Override
    public GenerateKeyPairMethod makeGenerateKeyPairMethod(GenerateKeyPairMethod.Args args) {
        return new GenerateKeyPairMethodImpl(args);
    }

    @Override
    public GetAttributeMethod makeGetAttributeMethod(GetAttributeMethod.Args args) {
        return new GetAttributeMethodImpl(args);
    }

    @Override
    public SignInitMethod makeSignInitMethod(SignInitMethod.Args args) {
        return new SignInitMethodImpl(args);
    }

    @Override
    public SignMethod makeSignMethod(SignMethod.Args args) {
        return new SignMethodImpl(args);
    }
}
