/*
    Copyright 2013 NIC Chile Research Labs
    This file is part of TsCrypto.

    TsCrypto is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TsCrypto is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TsCrypto.  If not, see <http://www.gnu.org/licenses/>.
 */

package cl.niclabs.cb.common.methods;

import cl.niclabs.cb.common.*;
import cl.niclabs.cb.common.methods.*;

public class MethodFactoryImpl implements MethodFactory {

    private final SessionManager sessionManager;
    private final SessionFactory sessionFactory;
    private final KeyOperations keyOperations;

    public MethodFactoryImpl(SessionManager sessionManager, SessionFactory sessionFactory, KeyOperations keyOperations) {
        this.sessionManager = sessionManager;
        this.sessionFactory = sessionFactory;
        this.keyOperations = keyOperations;
    }

    @Override
    public Method makeOpenSessionMethod() {
        return new OpenSessionMethod(sessionManager, sessionFactory);
    }

    @Override
    public Method makeCloseSessionMethod(CloseSessionMethod.Args args) {
        return new CloseSessionMethod(args, sessionManager);
    }

    @Override
    public Method makeDeleteKeyMethod(DeleteKeyPairMethod.Args args) {
        return new DeleteKeyPairMethod(args, keyOperations);
    }

    @Override
    public Method makeFindKeyMethod(FindKeyMethod.Args args) {
        return new FindKeyMethod(args, keyOperations);
    }

    @Override
    public Method makeGenerateKeyPairMethod(GenerateKeyPairMethod.Args args) {
        return new GenerateKeyPairMethod(args, keyOperations);
    }

    @Override
    public Method makeSignInitMethod(SignInitMethod.Args args) {
        return new SignInitMethod(args, sessionManager);
    }

    @Override
    public Method makeSignMethod(SignMethod.Args args) {
        return new SignMethod(args, sessionManager);
    }

    @Override
    public Method makeSeedRandomMethod(SeedRandomMethod.Args args) {
        return new SeedRandomMethod(args, sessionManager);
    }

    @Override
    public Method makeGenerateRandomMethod(GenerateRandomMethod.Args args) {
        return new GenerateRandomMethod(args, sessionManager);
    }

    @Override
    public Method makeDigestInitMethod(DigestInitMethod.Args args) {
        return new DigestInitMethod(args, sessionManager);
    }

    @Override
    public Method makeDigestMethod(DigestMethod.Args args) {
        return new DigestMethod(args, sessionManager);
    }
}
