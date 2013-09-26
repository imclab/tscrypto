package cb.backend.methods;

public interface MethodsFactory {
    DeleteKeyPairMethod makeDeleteKeyMethod(DeleteKeyPairMethod.Args args);
    FindKeyMethod makeFindKeyMethod(FindKeyMethod.Args args);
    GenerateKeyPairMethod makeGenerateKeyPairMethod(GenerateKeyPairMethod.Args args);
    GetAttributeMethod makeGetAttributeMethod(GetAttributeMethod.Args args);
    SignInitMethod makeSignInitMethod(SignInitMethod.Args args);
    SignMethod makeSignMethod(SignMethod.Args args);
}
