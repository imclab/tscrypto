package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;

public interface MethodFactory {
    Method makeDeleteKeyMethod(DeleteKeyPairMethod.Args args);
    Method makeFindKeyMethod(FindKeyMethod.Args args);
    Method makeGenerateKeyPairMethod(GenerateKeyPairMethod.Args args);
    Method makeSignInitMethod(SignInitMethod.Args args);
    Method makeSignMethod(SignMethod.Args args);
    Method makeSeedRandomMethod(SeedRandomMethod.Args args);
    Method makeGenerateRandomMethod(GenerateRandomMethod.Args args);
    Method makeDigestInitMethod(DigestInitMethod.Args args);
    Method makeDigestMethod(DigestMethod.Args args);
}
