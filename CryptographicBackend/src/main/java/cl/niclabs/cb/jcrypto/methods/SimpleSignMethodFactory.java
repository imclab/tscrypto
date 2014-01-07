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
