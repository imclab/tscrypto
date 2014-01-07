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


package cl.niclabs.cb.tscrypto;

import cl.niclabs.tscrypto.sigDealer.KeyManager;
import cl.niclabs.tscrypto.sigDealer.RequestManager;
import cl.niclabs.cb.backend.Digester;
import cl.niclabs.cb.backend.RandomGenerator;
import cl.niclabs.cb.backend.Session;
import cl.niclabs.cb.backend.Signer;
import cl.niclabs.cb.jcrypto.DigesterImpl;
import cl.niclabs.cb.jcrypto.RandomGeneratorImpl;

import java.util.UUID;

public class SessionImpl implements Session {
    private UUID handler;
    private Signer signer;
    private Digester digester;
    private RandomGenerator randomGenerator;

    public SessionImpl(KeyManager keyManager, RequestManager requestManager) {
        this.handler = UUID.randomUUID();
        this.signer = new SignerImpl(keyManager, requestManager);
        this.digester = new DigesterImpl();
        this.randomGenerator = new RandomGeneratorImpl();
    }

    @Override
    public String getHandler() {
        return handler.toString();
    }

    @Override
    public Signer getSigner() {
        return signer;
    }

    @Override
    public Digester getDigester() {
        return digester;
    }

    @Override
    public RandomGenerator getRandomGenerator() {
        return randomGenerator;
    }

    @Override
    public String toString() {
        return getHandler();
    }
}
