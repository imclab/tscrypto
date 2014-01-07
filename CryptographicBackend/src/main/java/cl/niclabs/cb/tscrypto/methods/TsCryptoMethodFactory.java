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

package cl.niclabs.cb.tscrypto.methods;


import cl.niclabs.tscrypto.sigDealer.*;
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

    private RequestManager requestManager;
    private SessionManager sessionManager;

    public TsCryptoMethodFactory(RequestManager requestManager) throws IOException {
        //keyManager = new HashTableKeyManager();
        try {
            keyManager = new H2KeyManager();
        } catch (ClassNotFoundException | SQLException e ) {
            throw new IOException(e);
        }

        sessionManager = new SessionManagerImpl();

        this.requestManager = requestManager;

        k = SDConfig.getInstance().getK();
        l = SDConfig.getInstance().getL();
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
        return new DeleteKeyPairMethodImpl(args, keyManager, requestManager);
    }

    @Override
    public Method makeFindKeyMethod(FindKeyMethod.Args args) {
        return new FindKeyMethodImpl(args, keyManager);
    }

    @Override
    public Method makeGenerateKeyPairMethod(GenerateKeyPairMethod.Args args) {
        return new GenerateKeyPairMethodImpl(args, keyManager, requestManager, k, l);
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
